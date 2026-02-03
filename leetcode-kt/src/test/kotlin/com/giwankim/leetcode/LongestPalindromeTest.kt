package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class LongestPalindromeTest :
    FunSpec({
        val sut = LongestPalindrome()

        context("longest palindrome") {
            withTests(
                nameFn = { (s, expected) -> "s=$s, expected=$expected" },
                LongestPalindromeCase("a", "a"),
                LongestPalindromeCase("ab", "a"),
                LongestPalindromeCase("babad", "bab"),
                LongestPalindromeCase("cbbd", "bb"),
                LongestPalindromeCase("dcbabcdd", "dcbabcd"),
            ) { (s, expected) ->
                sut.longestPalindrome(s) shouldBe expected
            }
        }
    })

private data class LongestPalindromeCase(
    val s: String,
    val expected: String,
)
