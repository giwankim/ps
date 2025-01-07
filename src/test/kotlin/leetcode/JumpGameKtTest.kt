package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class JumpGameKtTest :
    FunSpec(
        {
            context("canJump") {
                val sut = JumpGameKt()
                withData(
                    nameFn = { "${it.nums}, ${it.expected}" },
                    JumpGameTestCase(listOf(2, 3, 1, 1, 4), true),
                    JumpGameTestCase(listOf(3, 2, 1, 0, 4), false),
                ) { (nums, expected) ->
                    sut.canJump(nums.toIntArray()) shouldBe expected
                }
            }
        },
    )

data class JumpGameTestCase(
    val nums: List<Int>,
    val expected: Boolean,
)
