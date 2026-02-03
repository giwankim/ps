package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class JumpGameIITest :
    FunSpec(
        {
            val sut = JumpGameII()

            context("jump game ii") {
                withTests(
                    nameFn = { "${it.nums.contentToString()}, ${it.expected}" },
                    JumpGameIITestCase(intArrayOf(2, 3, 1, 1, 4), 2),
                    JumpGameIITestCase(intArrayOf(2, 3, 0, 1, 4), 2),
                ) { (nums, expected) ->
                    sut.jump(nums) shouldBe expected
                }
            }
        },
    )

data class JumpGameIITestCase(
    val nums: IntArray,
    val expected: Int,
)
