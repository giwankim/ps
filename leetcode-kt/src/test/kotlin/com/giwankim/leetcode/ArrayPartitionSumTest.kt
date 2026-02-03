package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class ArrayPartitionSumTest :
    FunSpec({
        val sut = ArrayPartitionSum()

        context("array partition sum") {
            withTests(
                nameFn = { (nums, expected) -> "nums=${nums.contentToString()}, expected=$expected" },
                ArrayPartitionSumCase(intArrayOf(1, 4, 3, 2), 4),
                ArrayPartitionSumCase(intArrayOf(6, 2, 6, 5, 1, 2), 9),
            ) { (nums, expected) ->
                sut.arrayPairSum(nums) shouldBe expected
            }
        }
    })

private data class ArrayPartitionSumCase(
    val nums: IntArray,
    val expected: Int,
)
