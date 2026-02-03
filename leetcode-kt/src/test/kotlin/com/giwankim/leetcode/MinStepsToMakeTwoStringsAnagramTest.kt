package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class MinStepsToMakeTwoStringsAnagramTest :
    FunSpec({
        val sut = MinStepsToMakeTwoStringsAnagram()

        context("min steps to make two strings anagram") {
            withTests(
                nameFn = { (s, t, expected) -> "s=$s, t=$t, expected=$expected" },
                MinStepsToMakeTwoStringsAnagramCase("bab", "aba", 1),
                MinStepsToMakeTwoStringsAnagramCase("leetcode", "practice", 5),
                MinStepsToMakeTwoStringsAnagramCase("anagram", "mangaar", 0),
            ) { (s, t, expected) ->
                sut.minSteps(s, t) shouldBe expected
            }
        }
    })

private data class MinStepsToMakeTwoStringsAnagramCase(
    val s: String,
    val t: String,
    val expected: Int,
)
