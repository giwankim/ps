package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class SlidingWindowMaximumTest :
    FunSpec({
        val sut = SlidingWindowMaximum()

        context("sliding window maximum") {
            withTests(
                nameFn = { (nums, k, expected) ->
                    "nums=${nums.contentToString()}, k=$k, expected=${expected.contentToString()}"
                },
                SlidingWindowMaximumCase(intArrayOf(1, 3, -1, -3, 5, 3, 6, 7), 3, intArrayOf(3, 3, 5, 5, 6, 7)),
                SlidingWindowMaximumCase(intArrayOf(1), 1, intArrayOf(1)),
            ) { (nums, k, expected) ->
                sut.maxSlidingWindow(nums, k) shouldBe expected
            }
        }
    })

private data class SlidingWindowMaximumCase(
    val nums: IntArray,
    val k: Int,
    val expected: IntArray,
)
