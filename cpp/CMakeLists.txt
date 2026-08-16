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
