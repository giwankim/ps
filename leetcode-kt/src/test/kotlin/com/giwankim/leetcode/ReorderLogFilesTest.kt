package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class ReorderLogFilesTest :
    FunSpec({
        val sut = ReorderLogFiles()

        context("reorder log files") {
            withTests(
                nameFn = { (logs, expected) ->
                    "logs=${logs.contentToString()}, expected=${expected.contentToString()}"
                },
                ReorderLogFilesCase(
                    arrayOf("dig1 8 1 5 1", "let1 art can", "dig2 3 6", "let2 own kit dig", "let3 art zero"),
                    arrayOf("let1 art can", "let3 art zero", "let2 own kit dig", "dig1 8 1 5 1", "dig2 3 6"),
                ),
                ReorderLogFilesCase(
                    arrayOf("a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo"),
                    arrayOf("g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"),
                ),
            ) { (logs, expected) ->
                sut.reorderLogFiles(logs) shouldBe expected
            }
        }
    })

private data class ReorderLogFilesCase(
    val logs: Array<String>,
    val expected: Array<String>,
)
