package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ThreeSumTest {
    @ParameterizedTest
    @MethodSource
    fun threeSum(
        nums: IntArray,
        expected: List<List<Int>>,
    ) {
        val actual = ThreeSum().threeSum(nums)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun threeSum(): List<Arguments> = listOf(
            Arguments.of(intArrayOf(-1, 0, 1), listOf(listOf(-1, 0, 1))),
            Arguments.of(intArrayOf(-1, -1, 0, 1, 2), listOf(listOf(-1, -1, 2), listOf(-1, 0, 1))),
            Arguments.of(intArrayOf(-1, 0, 1, 2, -1, 4), listOf(listOf(-1, -1, 2), listOf(-1, 0, 1))),
            Arguments.of(intArrayOf(0, 1, 1), listOf<List<Int>>()),
            Arguments.of(intArrayOf(0, 0, 0), listOf(listOf(0, 0, 0))),
        )
    }
}
