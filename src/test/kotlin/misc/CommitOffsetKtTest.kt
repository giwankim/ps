package misc

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class CommitOffsetKtTest :
    FunSpec(
        {
            lateinit var sut: CommitOffsetKt

            beforeTest { sut = CommitOffsetKt() }

            test("in order") {
                val offsets = intArrayOf(0, 1, 2)
                sut.commitOffsets(offsets) shouldBe intArrayOf(0, 1, 2)
            }

            test("out of order") {
                val offsets = intArrayOf(2, 0, 1)
                sut.commitOffsets(offsets) shouldBe intArrayOf(-1, 0, 2)
            }

            context("general cases") {
                withData<CommitOffsetTestCase>(
                    CommitOffsetTestCase(offsets = intArrayOf(2, 1, 0, 5, 4), expected = intArrayOf(-1, -1, 2, -1, -1)),
                    CommitOffsetTestCase(offsets = intArrayOf(2, 1, 0, 5, 4, 3), expected = intArrayOf(-1, -1, 2, -1, -1, 5)),
                    CommitOffsetTestCase(
                        offsets = intArrayOf(2, 1, 0, 5, 4, 3, 9, 7, 6, 8),
                        expected = intArrayOf(-1, -1, 2, -1, -1, 5, -1, -1, 7, 9),
                    ),
                    CommitOffsetTestCase(
                        offsets = intArrayOf(3, 0, 2, 4, 1, 7, 6, 5, 9),
                        expected = intArrayOf(-1, 0, -1, -1, 4, -1, -1, 7, -1),
                    ),
                ) { (offsets, expected) ->
                    sut.commitOffsets(offsets) shouldBe expected
                }
            }
        },
    )

data class CommitOffsetTestCase(
    val offsets: IntArray,
    val expected: IntArray,
) : WithDataTestName {
    override fun dataTestName(): String = "offsets=${offsets.joinToString(",")} -> expected=${expected.joinToString(",")}"
}
