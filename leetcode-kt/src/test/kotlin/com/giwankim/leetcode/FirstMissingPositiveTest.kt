package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class FirstMissingPositiveTest :
    FunSpec(
        {
            val sut = FirstMissingPositive()

            context("first missing positive") {
                withTests(
                    nameFn = { (nums, expected) -> "nums=${nums.contentToString()}, expected=$expected" },
                    FirstMissingPositiveTestCase(intArrayOf(1, 2, 0), 3),
                    FirstMissingPositiveTestCase(intArrayOf(3, 4, -1, 1), 2),
                    FirstMissingPositiveTestCase(intArrayOf(7, 8, 9, 11, 12), 1),
                ) { (nums, expected) ->
                    sut.firstMissingPositive(nums) shouldBe expected
                }
            }
        },
    )

data class FirstMissingPositiveTestCase(
    val nums: IntArray,
    val expected: Int,
)
