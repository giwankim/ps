package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MergeIntervalsKtTest :
    FunSpec(
        {
            lateinit var sut: MergeIntervalsKt

            beforeTest { sut = MergeIntervalsKt() }

            context("merge intervals") {
                test("two intervals do not overlap") {
                    val intervals = arrayOf(intArrayOf(1, 2), intArrayOf(3, 4))
                    val expected = arrayOf(intArrayOf(1, 2), intArrayOf(3, 4))
                    sut.merge(intervals) shouldBe expected
                }

                test("two intervals overlap") {
                    val intervals = arrayOf(intArrayOf(1, 4), intArrayOf(4, 5))
                    val expected = arrayOf(intArrayOf(1, 5))
                    sut.merge(intervals) shouldBe expected
                }

                test("merge intervals in more general case") {
                    val intervals =
                        arrayOf(
                            intArrayOf(1, 3),
                            intArrayOf(2, 6),
                            intArrayOf(8, 10),
                            intArrayOf(15, 18),
                        )
                    val expected =
                        arrayOf(
                            intArrayOf(1, 6),
                            intArrayOf(8, 10),
                            intArrayOf(15, 18),
                        )
                    sut.merge(intervals) shouldBe expected
                }
            }
        },
    )
