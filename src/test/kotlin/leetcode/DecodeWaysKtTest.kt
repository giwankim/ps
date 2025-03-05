package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DecodeWaysKtTest :
    FunSpec(
        {
            lateinit var sut: DecodeWaysKt

            beforeTest { sut = DecodeWaysKt() }

            test("no way") {
                sut.numDecodings("06") shouldBe 0
            }

            test("one way") {
                sut.numDecodings("1") shouldBe 1
            }

            test("two ways") {
                sut.numDecodings("12") shouldBe 2
            }

            test("general case") {
                sut.numDecodings("226") shouldBe 3
            }
        },
    )
