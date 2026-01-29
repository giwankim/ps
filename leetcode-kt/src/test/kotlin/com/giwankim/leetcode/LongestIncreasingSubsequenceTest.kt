package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class LongestIncreasingSubsequenceTest :
    FunSpec(
        {
            context("longest increasing subsequence") {
                withData(
                    LongestIncreasingSubsequenceTestCase(intArrayOf(10, 9, 2, 5, 3, 7, 101, 18), 4),
                    LongestIncreasingSubsequenceTestCase(intArrayOf(0, 1, 0, 3, 2, 3), 4),
                    LongestIncreasingSubsequenceTestCase(intArrayOf(7, 7, 7, 7, 7, 7, 7), 1),
                ) { (nums, expected) ->
                    val sut = LongestIncreasingSubsequence()
                    sut.lengthOfLIS(nums) shouldBe expected
                }
            }
        },
    )

data class LongestIncreasingSubsequenceTestCase(
    val nums: IntArray,
    val expected: Int,
) : WithDataTestName {
    override fun dataTestName(): String = "nums=${nums.contentToString()}, expected=$expected"
}
