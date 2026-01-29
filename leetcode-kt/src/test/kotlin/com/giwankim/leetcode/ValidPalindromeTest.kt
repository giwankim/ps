package com.giwankim.leetcode

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ValidPalindromeTest {
    @ParameterizedTest
    @MethodSource("testCases")
    fun `should check whether palindrome`(
        s: String,
        expected: Boolean,
    ) {
        val sut = ValidPalindrome()
        Assertions.assertThat(sut.isPalindrome(s))
    }

    companion object {
        @JvmStatic
        fun testCases() = listOf(
            Arguments.of("A man, a plan, a canal: Panama", true),
            Arguments.of("race a car", false),
            Arguments.of(" ", true),
            Arguments.of("Do geese see God?", true),
            Arguments.of("Hannah", true),
            Arguments.of("Hang up!", false),
        )
    }
}
