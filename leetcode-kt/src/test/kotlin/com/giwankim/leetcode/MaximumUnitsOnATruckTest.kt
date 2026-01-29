package com.giwankim.leetcode

import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class MaximumUnitsOnATruckTest {
    @ParameterizedTest
    @MethodSource
    fun maximumUnits(
        boxTypes: Array<IntArray>,
        truckSize: Int,
        expected: Int,
    ) {
        val sut = MaximumUnitsOnATruck()
        sut.maximumUnits(boxTypes, truckSize) shouldBe expected
    }

    companion object {
        @JvmStatic
        fun maximumUnits() = listOf(
            Arguments.of(arrayOf(intArrayOf(1, 3), intArrayOf(2, 2), intArrayOf(3, 1)), 4, 8),
            Arguments.of(arrayOf(intArrayOf(5, 10), intArrayOf(2, 5), intArrayOf(4, 7), intArrayOf(3, 9)), 10, 91),
        )
    }
}
