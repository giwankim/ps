package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class BinarySearchTest :
    FunSpec({
        val sut = BinarySearch()

        context("binary search") {
            withTests(
                nameFn = { (nums, target, expected) -> "nums=${nums.contentToString()}, target=$target, expected=$expected" },
                BinarySearchCase(intArrayOf(-1, 0, 3, 5, 9, 12), 9, 4),
                BinarySearchCase(intArrayOf(-1, 0, 3, 5, 9, 12), 2, -1),
                BinarySearchCase(intArrayOf(5), 5, 0),
            ) { (nums, target, expected) ->
                sut.search(nums, target) shouldBe expected
            }
        }
    })

private data class BinarySearchCase(
    val nums: IntArray,
    val target: Int,
    val expected: Int,
)
