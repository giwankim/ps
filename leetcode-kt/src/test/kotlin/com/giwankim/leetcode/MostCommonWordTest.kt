package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class MostCommonWordTest {
    @ParameterizedTest
    @MethodSource
    fun mostCommonWord(
        paragraph: String,
        banned: Array<String>,
        expected: String,
    ) {
        val actual = MostCommonWord().mostCommonWord(paragraph, banned)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun mostCommonWord(): List<Arguments> = listOf(
            Arguments.of("a.", emptyArray<String>(), "a"),
            Arguments.of("Bob hit a ball, the hit BALL flew far after it was hit.", arrayOf("hit"), "ball"),
        )
    }
}
