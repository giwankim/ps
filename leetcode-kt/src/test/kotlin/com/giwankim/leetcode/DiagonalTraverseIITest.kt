package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class DiagonalTraverseIITest :
    FunSpec(
        {
            context("diagonal traverse II") {
                withData(
                    DiagonalTraverseIITestCase(
                        listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9)),
                        intArrayOf(1, 4, 2, 7, 5, 3, 8, 6, 9),
                    ),
                    DiagonalTraverseIITestCase(
                        listOf(listOf(1, 2, 3, 4, 5), listOf(6, 7), listOf(8), listOf(9, 10, 11), listOf(12, 13, 14, 15, 16)),
                        intArrayOf(1, 6, 2, 8, 7, 3, 9, 4, 12, 10, 5, 13, 11, 14, 15, 16),
                    ),
                ) { (nums, expected) ->
                    val sut = DiagonalTraverseII()
                    sut.findDiagonalOrder(nums) shouldBe expected
                }
            }
        },
    )

data class DiagonalTraverseIITestCase(
    val nums: List<List<Int>>,
    val expected: IntArray,
) : WithDataTestName {
    override fun dataTestName(): String = "nums=$nums, expected=${expected.contentToString()}"
}
