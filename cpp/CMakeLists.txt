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

# Split PS_SCOPE once: "all" | "<judge>" | "<judge>/<problem>". Compared with STREQUAL
# rather than MATCHES so a judge or problem name is never interpreted as a regex.
set(_scope_judge "")
set(_scope_problem "")
if(NOT PS_SCOPE STREQUAL "all")
  string(FIND "${PS_SCOPE}" "/" _slash)
  if(_slash EQUAL -1)
    set(_scope_judge "${PS_SCOPE}")
  else()
    string(SUBSTRING "${PS_SCOPE}" 0 ${_slash} _scope_judge)
    math(EXPR _after "${_slash} + 1")
    string(SUBSTRING "${PS_SCOPE}" ${_after} -1 _scope_problem)
  endif()
  # A trailing slash ("boj/") leaves an empty problem, which would disable the
  # per-problem filter below and silently widen the scope to the whole judge —
  # the opposite of what a scope guard is for.
  if(_scope_judge STREQUAL ""
     OR _scope_problem MATCHES "/"
     OR (NOT _slash EQUAL -1 AND _scope_problem STREQUAL ""))
    message(FATAL_ERROR
      "cpp: PS_SCOPE must be 'all', '<judge>', or '<judge>/<problem>' (got '${PS_SCOPE}')")
  endif()
endif()

set(missing_tests "")
set(_scope_matched FALSE)
foreach(judge IN LISTS judges)
  # Skip judges outside PS_SCOPE. PS_SCOPE is "all", a judge name, or "<judge>/<problem>".
  if(NOT _scope_judge STREQUAL "" AND NOT _scope_judge STREQUAL "${judge}")
    continue()
  endif()

  file(GLOB sources CONFIGURE_DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/${judge}/*.cpp")
  foreach(src IN LISTS sources)
    # NAME_WLE, not NAME_WE: NAME_WE strips from the first dot, so "1000.alt.cpp"
    # would collapse to "1000" and collide with 1000.cpp's target and data dir.
    get_filename_component(problem "${src}" NAME_WLE)
    # A "<judge>/<problem>" scope narrows to exactly one problem.
    if(NOT _scope_problem STREQUAL "" AND NOT _scope_problem STREQUAL "${problem}")
      continue()
    endif()
    set(_scope_matched TRUE)
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
      # Likewise NAME_WLE: a dotted stem such as "sample.1.in" must pair with
      # "sample.1.out", not be truncated to "sample" and rejected as unpaired.
      get_filename_component(stem "${input}" NAME_WLE)
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

# A scope that matched a real judge/problem but has no test data yet is fine (the
# WARNING above covers it). A scope that matched nothing at all is always a typo and
# must not configure a silent zero-test success.
if(NOT PS_SCOPE STREQUAL "all" AND NOT _scope_matched)
  message(FATAL_ERROR "cpp: PS_SCOPE='${PS_SCOPE}' matched no problem. Check the judge and problem names.")
endif()
