package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class LetterCombinationOfAPhoneNumberTest {
    @ParameterizedTest
    @MethodSource
    fun letterCombinations(
        digits: String,
        expected: List<String>,
    ) {
        val actual = LetterCombinationOfAPhoneNumber().letterCombinations(digits)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun letterCombinations(): List<Arguments> = listOf(
            Arguments.of(
                "23",
                listOf("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"),
            ),
            Arguments.of("", emptyList<String>()),
            Arguments.of("2", listOf("a", "b", "c")),
        )
    }
}
