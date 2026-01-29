package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class TwoSumTest {
    @ParameterizedTest
    @MethodSource
    fun twoSum(
        nums: IntArray,
        target: Int,
        expected: IntArray,
    ) {
        val actual = TwoSum().twoSum(nums, target)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun twoSum(): List<Arguments> = listOf(
            Arguments.of(intArrayOf(2, 7, 11, 15), 9, intArrayOf(0, 1)),
            Arguments.of(intArrayOf(3, 2, 4), 6, intArrayOf(1, 2)),
            Arguments.of(intArrayOf(3, 3), 6, intArrayOf(0, 1)),
        )
    }
}
