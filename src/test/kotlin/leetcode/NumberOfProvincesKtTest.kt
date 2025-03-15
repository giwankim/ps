package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class NumberOfProvincesKtTest :
    FunSpec(
        {
            lateinit var sut: NumberOfProvincesKt

            beforeTest { sut = NumberOfProvincesKt() }

            test("islands") {
                val isConnected = arrayOf(intArrayOf(1, 0, 0), intArrayOf(0, 1, 0), intArrayOf(0, 0, 1))
                sut.findCircleNum(isConnected) shouldBe 3
            }

            test("two provinces") {
                val isConnected = arrayOf(intArrayOf(1, 1, 0), intArrayOf(1, 1, 0), intArrayOf(0, 0, 1))
                sut.findCircleNum(isConnected) shouldBe 2
            }
        },
    )
