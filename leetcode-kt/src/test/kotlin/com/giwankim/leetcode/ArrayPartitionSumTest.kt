package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ArrayPartitionSumTest {
    @ParameterizedTest
    @MethodSource
    fun arrayPairSum(
        nums: IntArray,
        expected: Int,
    ) {
        val actual = ArrayPartitionSum().arrayPairSum(nums)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun arrayPairSum(): List<Arguments> = listOf(
            Arguments.of(intArrayOf(1, 4, 3, 2), 4),
            Arguments.of(intArrayOf(6, 2, 6, 5, 1, 2), 9),
        )
    }
}
