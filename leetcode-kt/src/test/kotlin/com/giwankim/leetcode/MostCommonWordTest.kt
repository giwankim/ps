package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class MostCommonWordTest :
    FunSpec({
        val sut = MostCommonWord()

        context("most common word") {
            withTests(
                nameFn = { (paragraph, banned, expected) ->
                    "paragraph=${paragraph.take(20)}..., banned=${banned.contentToString()}, expected=$expected"
                },
                MostCommonWordCase("a.", emptyArray(), "a"),
                MostCommonWordCase("Bob hit a ball, the hit BALL flew far after it was hit.", arrayOf("hit"), "ball"),
            ) { (paragraph, banned, expected) ->
                sut.mostCommonWord(paragraph, banned) shouldBe expected
            }
        }
    })

private data class MostCommonWordCase(
    val paragraph: String,
    val banned: Array<String>,
    val expected: String,
)
