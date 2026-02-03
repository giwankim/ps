package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class TwoSumTest :
    FunSpec({
        val sut = TwoSum()

        context("two sum") {
            withTests(
                nameFn = { (nums, target, expected) ->
                    "nums=${nums.contentToString()}, target=$target, expected=${expected.contentToString()}"
                },
                TwoSumCase(intArrayOf(2, 7, 11, 15), 9, intArrayOf(0, 1)),
                TwoSumCase(intArrayOf(3, 2, 4), 6, intArrayOf(1, 2)),
                TwoSumCase(intArrayOf(3, 3), 6, intArrayOf(0, 1)),
            ) { (nums, target, expected) ->
                sut.twoSum(nums, target) shouldBe expected
            }
        }
    })

private data class TwoSumCase(
    val nums: IntArray,
    val target: Int,
    val expected: IntArray,
)
