package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class FirstMissingPositiveTest :
    FunSpec(
        {
            context("first missing positive") {
                withData(
                    FirstMissingPositiveTestCase(intArrayOf(1, 2, 0), 3),
                    FirstMissingPositiveTestCase(intArrayOf(3, 4, -1, 1), 2),
                    FirstMissingPositiveTestCase(intArrayOf(7, 8, 9, 11, 12), 1),
                ) { (nums, expected) ->
                    val sut = FirstMissingPositive()
                    sut.firstMissingPositive(nums) shouldBe expected
                }
            }
        },
    )

data class FirstMissingPositiveTestCase(
    val nums: IntArray,
    val expected: Int,
) : WithDataTestName {
    override fun dataTestName(): String = "nums = ${nums.contentToString()}, expected = $expected"
}
