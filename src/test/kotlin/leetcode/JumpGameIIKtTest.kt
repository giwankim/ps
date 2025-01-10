package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class JumpGameIIKtTest :
    FunSpec(
        {
            context("jump") {
                withData(
                    nameFn = { "${it.nums.contentToString()}, ${it.expected}" },
                    JumpGameIITestCase(intArrayOf(2, 3, 1, 1, 4), 2),
                    JumpGameIITestCase(intArrayOf(2, 3, 0, 1, 4), 2),
                ) { (nums, expected) ->
                    val sut = JumpGameIIKt()
                    sut.jump(nums) shouldBe expected
                }
            }
        },
    )

data class JumpGameIITestCase(
    val nums: IntArray,
    val expected: Int,
)
