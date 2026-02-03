package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class ThreeSumTest :
    FunSpec({
        val sut = ThreeSum()

        context("three sum") {
            withTests(
                nameFn = { (nums, expected) ->
                    "nums=${nums.contentToString()}, expected=$expected"
                },
                ThreeSumCase(intArrayOf(-1, 0, 1), listOf(listOf(-1, 0, 1))),
                ThreeSumCase(intArrayOf(-1, -1, 0, 1, 2), listOf(listOf(-1, -1, 2), listOf(-1, 0, 1))),
                ThreeSumCase(intArrayOf(-1, 0, 1, 2, -1, 4), listOf(listOf(-1, -1, 2), listOf(-1, 0, 1))),
                ThreeSumCase(intArrayOf(0, 1, 1), listOf()),
                ThreeSumCase(intArrayOf(0, 0, 0), listOf(listOf(0, 0, 0))),
            ) { (nums, expected) ->
                sut.threeSum(nums) shouldBe expected
            }
        }
    })

private data class ThreeSumCase(
    val nums: IntArray,
    val expected: List<List<Int>>,
)
