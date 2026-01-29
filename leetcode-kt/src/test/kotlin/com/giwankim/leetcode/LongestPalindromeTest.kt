package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class LongestPalindromeTest {
    @ParameterizedTest
    @MethodSource
    fun longestPalindrome(
        s: String,
        expected: String,
    ) {
        val actual = LongestPalindrome().longestPalindrome(s)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun longestPalindrome(): List<Arguments> = listOf(
            Arguments.of("a", "a"),
            Arguments.of("ab", "a"),
            Arguments.of("babad", "bab"),
            Arguments.of("cbbd", "bb"),
            Arguments.of("dcbabcdd", "dcbabcd"),
        )
    }
}
