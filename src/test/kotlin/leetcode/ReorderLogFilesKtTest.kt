package leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ReorderLogFilesKtTest {
    @ParameterizedTest
    @MethodSource
    fun reorderLogFiles(
        logs: Array<String>,
        expected: Array<String>,
    ) {
        val actual = ReorderLogFilesKt().reorderLogFiles(logs)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun reorderLogFiles(): List<Arguments> =
            listOf(
                Arguments.of(
                    arrayOf("dig1 8 1 5 1", "let1 art can", "dig2 3 6", "let2 own kit dig", "let3 art zero"),
                    arrayOf("let1 art can", "let3 art zero", "let2 own kit dig", "dig1 8 1 5 1", "dig2 3 6"),
                ),
                Arguments.of(
                    arrayOf("a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo"),
                    arrayOf("g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"),
                ),
            )
    }
}
