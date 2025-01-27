package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TopKFrequentElementsKtTest :
    FunSpec(
        {
            context("topKFrequent") {
                val sut = TopKFrequentElementsKt()

                test("singleton") {
                    val nums = intArrayOf(1)
                    val k = 1
                    sut.topKFrequent(nums, k).toList() shouldBe listOf(1)
                }

                test("multiple elements") {
                    val nums = intArrayOf(1, 1, 1, 2, 2, 3)
                    val k = 2
                    sut.topKFrequent(nums, k).toList() shouldBe listOf(1, 2)
                }
            }
        },
    )
