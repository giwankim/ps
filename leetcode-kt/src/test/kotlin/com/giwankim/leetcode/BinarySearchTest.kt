package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class BinarySearchTest {
    @ParameterizedTest
    @MethodSource
    fun search(
        nums: IntArray,
        target: Int,
        expected: Int,
    ) {
        val actual = BinarySearch().search(nums, target)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun search(): List<Arguments> = listOf(
            Arguments.of(intArrayOf(-1, 0, 3, 5, 9, 12), 9, 4),
            Arguments.of(intArrayOf(-1, 0, 3, 5, 9, 12), 2, -1),
            Arguments.of(intArrayOf(5), 5, 0),
        )
    }
}
