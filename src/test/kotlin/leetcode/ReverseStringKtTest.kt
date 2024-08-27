package leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ReverseStringKtTest {
    @ParameterizedTest
    @MethodSource
    fun `should reverse a string`(
        s: CharArray,
        expected: CharArray,
    ) {
        val reverseString = ReverseStringKt()
        reverseString.reverseString(s)
        assertThat(s).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun `should reverse a string`(): List<Arguments> =
            listOf(
                Arguments.of(charArrayOf('h', 'e', 'l', 'l', 'o'), charArrayOf('o', 'l', 'l', 'e', 'h')),
                Arguments.of(charArrayOf('H', 'a', 'n', 'n', 'a', 'h'), charArrayOf('h', 'a', 'n', 'n', 'a', 'H')),
            )
    }
}
