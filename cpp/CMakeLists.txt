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

foreach(judge IN LISTS judges)
  file(GLOB sources CONFIGURE_DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/${judge}/*.cpp")
  foreach(src IN LISTS sources)
    get_filename_component(problem "${src}" NAME_WE)
    string(REGEX REPLACE "[^A-Za-z0-9_.+-]" "_" safe "${problem}")
    add_executable("${judge}_${safe}" "${src}")
  endforeach()
endforeach()
