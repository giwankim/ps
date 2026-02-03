package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class CountBinarySubstringsTest :
    FunSpec({
        val sut = CountBinarySubstrings()

        context("count binary substrings") {
            withTests(
                nameFn = { (s, expected) -> "s=$s, expected=$expected" },
                CountBinarySubstringsCase("00110011", 6),
                CountBinarySubstringsCase("10101", 4),
            ) { (s, expected) ->
                sut.countBinarySubstrings(s) shouldBe expected
            }
        }
    })

private data class CountBinarySubstringsCase(
    val s: String,
    val expected: Int,
)
