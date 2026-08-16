cmake_minimum_required(VERSION 3.24)
project(ps-cpp CXX)

set(PS_SCOPE "all" CACHE STRING "Scope to configure: all | <judge> | <judge>/<problem>")
set(PS_STD "23" CACHE STRING "C++ standard to compile with")
set(PS_TIMEOUT "5" CACHE STRING "Per-test timeout in seconds")

set(CMAKE_CXX_STANDARD ${PS_STD})
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

enable_testing()

# The macOS main thread gets 8 MB of stack; judges give more. A DFS at depth 1e6
# must not segfault locally on code the judge accepts. Mirrors the -Xss128m that
# boj/build.gradle.kts sets for the same reason on the Java side.
if(APPLE)
  add_link_options(-Wl,-stack_size,0x4000000)
endif()

# --- discover judges -------------------------------------------------------
# A judge is any subdirectory of cpp/ other than cmake/ and build/. Adding a new
# judge therefore needs no edit to this file.
file(GLOB _entries CONFIGURE_DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/*")
set(judges "")
foreach(entry IN LISTS _entries)
  if(IS_DIRECTORY "${entry}")
    get_filename_component(_name "${entry}" NAME)
    if(NOT _name MATCHES "^(cmake|build)$")
      list(APPEND judges "${_name}")
    endif()
  endif()
endforeach()

set(missing_tests "")
foreach(judge IN LISTS judges)
  # Skip judges outside PS_SCOPE. PS_SCOPE is "all", a judge name, or "<judge>/<problem>".
  if(NOT PS_SCOPE STREQUAL "all"
     AND NOT PS_SCOPE STREQUAL "${judge}"
     AND NOT PS_SCOPE MATCHES "^${judge}/")
    continue()
  endif()

  file(GLOB sources CONFIGURE_DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/${judge}/*.cpp")
  foreach(src IN LISTS sources)
    get_filename_component(problem "${src}" NAME_WE)
    # A "<judge>/<problem>" scope narrows to exactly one problem.
    if(PS_SCOPE MATCHES "/" AND NOT PS_SCOPE STREQUAL "${judge}/${problem}")
      continue()
    endif()
    string(REGEX REPLACE "[^A-Za-z0-9_.+-]" "_" safe "${problem}")
    set(target "${judge}_${safe}")
    add_executable("${target}" "${src}")

    set(data_dir "${CMAKE_CURRENT_SOURCE_DIR}/${judge}/tests/${problem}")
    file(GLOB inputs CONFIGURE_DEPENDS "${data_dir}/*.in")
    if(NOT inputs)
      list(APPEND missing_tests "${judge}/${problem}")
      continue()
    endif()

    foreach(input IN LISTS inputs)
      get_filename_component(stem "${input}" NAME_WE)
      if(NOT EXISTS "${data_dir}/${stem}.out")
        message(FATAL_ERROR
          "cpp: ${judge}/${problem}/${stem}.in has no matching ${stem}.out")
      endif()
      set(test_name "${judge}/${problem}/${stem}")
      add_test(NAME "${test_name}"
               COMMAND ${CMAKE_COMMAND}
                       "-DEXE=$<TARGET_FILE:${target}>"
                       "-DINPUT=${input}"
                       "-DEXPECTED=${data_dir}/${stem}.out"
                       -P "${CMAKE_CURRENT_SOURCE_DIR}/cmake/RunCase.cmake")
      set_tests_properties("${test_name}" PROPERTIES TIMEOUT ${PS_TIMEOUT})
    endforeach()
  endforeach()
endforeach()

if(missing_tests)
  string(REPLACE ";" ", " _missing "${missing_tests}")
  message(WARNING "cpp: no test data for: ${_missing}")
endif()
