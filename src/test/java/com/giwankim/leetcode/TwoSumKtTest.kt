package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class TwoSumKtTest {
    @ParameterizedTest(name = "indices {2} sums to {1} in {0}")
    @MethodSource("testCases")
    fun `should return two sum indices`(
        nums: IntArray,
        target: Int,
        expected: IntArray,
    ) {
        val twoSum = TwoSumKt()
        assertThat(twoSum.twoSum(nums, target)).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun testCases() =
            listOf<Arguments>(
                Arguments.of(intArrayOf(2, 7, 11, 15), 9, intArrayOf(0, 1)),
                Arguments.of(intArrayOf(3, 2, 4), 6, intArrayOf(1, 2)),
                Arguments.of(intArrayOf(3, 3), 6, intArrayOf(0, 1)),
            )
    }
}
