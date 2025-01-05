package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class WordSearchKtTest :
    FunSpec(
        {
            test("exist") {
                arguments.forAll { (board, word, expected) ->
                    val sut = WordSearchKt()
                    val actual = sut.exist(board, word)
                    actual shouldBe expected
                }
            }
        },
    ) {
    companion object {
        private val arguments =
            listOf(
                Triple(arrayOf(charArrayOf('a')), "a", true),
                Triple(arrayOf(charArrayOf('a', 'b'), charArrayOf('c', 'd')), "abcd", false),
                Triple(
                    arrayOf(
                        charArrayOf('A', 'B', 'C', 'E'),
                        charArrayOf('S', 'F', 'C', 'S'),
                        charArrayOf('A', 'D', 'E', 'E'),
                    ),
                    "ABCCED",
                    true,
                ),
                Triple(
                    arrayOf(
                        charArrayOf('A', 'B', 'C', 'E'),
                        charArrayOf('S', 'F', 'C', 'S'),
                        charArrayOf('A', 'D', 'E', 'E'),
                    ),
                    "SEE",
                    true,
                ),
                Triple(
                    arrayOf(
                        charArrayOf('A', 'B', 'C', 'E'),
                        charArrayOf('S', 'F', 'C', 'S'),
                        charArrayOf('A', 'D', 'E', 'E'),
                    ),
                    "ABCB",
                    false,
                ),
            )
    }
}
