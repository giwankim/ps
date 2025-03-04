package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SimplifyPathKtTest :
    FunSpec(
        {
            lateinit var sut: SimplifyPathKt

            beforeTest { sut = SimplifyPathKt() }

            test("root") {
                sut.simplifyPath("/") shouldBe "/"
            }

            context("one depth") {
                test("base case") {
                    sut.simplifyPath("/home") shouldBe "/home"
                }

                test("trailing comma") {
                    sut.simplifyPath("/home/") shouldBe "/home"
                }

                test("cannot go up in depth from root") {
                    sut.simplifyPath("/../") shouldBe "/"
                }
            }

            context("two depth") {
                test("base case") {
                    sut.simplifyPath("/home/foo") shouldBe "/home/foo"
                }

                test("ignore consecutive slashes") {
                    sut.simplifyPath("/home//foo") shouldBe "/home/foo"
                }

                test(".. moves up one depth") {
                    sut.simplifyPath("/home/../foo") shouldBe "/foo"
                }
            }

            context("general case") {
                test("complex path") {
                    sut.simplifyPath("/home/user/Documents/../Pictures") shouldBe "/home/user/Pictures"
                }

                test("... is a valid directory name") {
                    sut.simplifyPath("/.../a/../b/c/../d/./") shouldBe "/.../b/d"
                }
            }
        },
    )
