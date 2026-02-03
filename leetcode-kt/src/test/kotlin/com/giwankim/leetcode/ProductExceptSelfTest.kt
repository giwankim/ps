package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class ProductExceptSelfTest :
    FunSpec({
        val sut = ProductExceptSelf()

        context("product except self") {
            withTests(
                nameFn = { (nums, expected) ->
                    "nums=${nums.contentToString()}, expected=${expected.contentToString()}"
                },
                ProductExceptSelfCase(intArrayOf(1, 2, 3, 4), intArrayOf(24, 12, 8, 6)),
                ProductExceptSelfCase(intArrayOf(-1, 1, 0, -3, 3), intArrayOf(0, 0, 9, 0, 0)),
            ) { (nums, expected) ->
                sut.productExceptSelf(nums) shouldBe expected
            }
        }
    })

private data class ProductExceptSelfCase(
    val nums: IntArray,
    val expected: IntArray,
)
