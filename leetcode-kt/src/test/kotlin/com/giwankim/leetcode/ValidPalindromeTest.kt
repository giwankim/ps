package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class ValidPalindromeTest :
    FunSpec({
        val sut = ValidPalindrome()

        context("valid palindrome") {
            withTests(
                nameFn = { (s, expected) -> "s=${s.take(20)}..., expected=$expected" },
                ValidPalindromeCase("A man, a plan, a canal: Panama", true),
                ValidPalindromeCase("race a car", false),
                ValidPalindromeCase(" ", true),
                ValidPalindromeCase("Do geese see God?", true),
                ValidPalindromeCase("Hannah", true),
                ValidPalindromeCase("Hang up!", false),
            ) { (s, expected) ->
                sut.isPalindrome(s) shouldBe expected
            }
        }
    })

private data class ValidPalindromeCase(
    val s: String,
    val expected: Boolean,
)
