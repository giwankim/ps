package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class MaxProfitTest {
    @ParameterizedTest
    @MethodSource
    fun maxProfit(
        prices: IntArray,
        expected: Int,
    ) {
        val actual = MaxProfit().maxProfit(prices)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun maxProfit() = listOf(
            Arguments.of(intArrayOf(7, 1, 5, 3, 6, 4), 5),
            Arguments.of(intArrayOf(7, 6, 4, 3, 1), 0),
        )
    }
}
