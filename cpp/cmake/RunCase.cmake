# Runs one solution binary against one .in file and compares stdout to its .out.
#
# Invoked by CTest as:
#   cmake -DEXE=<binary> -DINPUT=<file.in> -DEXPECTED=<file.out> -P RunCase.cmake
#
# The variable is INPUT rather than IN because IN is a foreach keyword in CMake.
#
# Comparison normalizes TRAILING whitespace only -- judges ignore it. Leading
# whitespace is preserved so a bug emitting a stray leading space still fails.

foreach(required EXE INPUT EXPECTED)
  if(NOT DEFINED ${required})
    message(FATAL_ERROR "RunCase.cmake: -D${required}= was not supplied")
  endif()
endforeach()

execute_process(
  COMMAND "${EXE}"
  INPUT_FILE "${INPUT}"
  OUTPUT_VARIABLE actual
  ERROR_VARIABLE stderr_text
  RESULT_VARIABLE exit_code)

if(NOT exit_code EQUAL 0)
  message(FATAL_ERROR "runtime error: exit ${exit_code}\n${stderr_text}")
endif()

file(READ "${EXPECTED}" expected)

function(ps_normalize text out_var)
  string(REPLACE "\r\n" "\n" text "${text}")
  string(REPLACE "\r" "\n" text "${text}")
  string(REGEX REPLACE "[ \t]+\n" "\n" text "${text}")
  string(REGEX REPLACE "[ \t\n]+$" "" text "${text}")
  set(${out_var} "${text}" PARENT_SCOPE)
endfunction()

ps_normalize("${actual}" actual)
ps_normalize("${expected}" expected)

if(actual STREQUAL expected)
  return()
endif()

# Escape ';' before splitting: CMake lists are semicolon-separated, so an
# unescaped ';' in the output would split one line into two.
function(ps_lines text out_var)
  string(REPLACE ";" "\\;" text "${text}")
  string(REPLACE "\n" ";" text "${text}")
  set(${out_var} "${text}" PARENT_SCOPE)
endfunction()

ps_lines("${expected}" expected_lines)
ps_lines("${actual}" actual_lines)
list(LENGTH expected_lines expected_count)
list(LENGTH actual_lines actual_count)

set(limit ${expected_count})
if(actual_count GREATER limit)
  set(limit ${actual_count})
endif()

math(EXPR last "${limit} - 1")
foreach(i RANGE 0 ${last})
  set(e "<missing>")
  set(a "<missing>")
  if(i LESS expected_count)
    list(GET expected_lines ${i} e)
  endif()
  if(i LESS actual_count)
    list(GET actual_lines ${i} a)
  endif()
  if(NOT e STREQUAL a)
    math(EXPR line_no "${i} + 1")
    message(FATAL_ERROR "output differs at line ${line_no}\n  expected: ${e}\n  actual:   ${a}")
  endif()
endforeach()

message(FATAL_ERROR "output differs but no differing line was found")
