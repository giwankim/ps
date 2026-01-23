package leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ValidParenthesesKtTest {
    @ParameterizedTest
    @MethodSource
    fun isValid(
        s: String,
        expected: Boolean,
    ) {
        val actual = ValidParenthesesKt().isValid(s)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun isValid(): List<Arguments> = listOf(
            Arguments.of("()", true),
            Arguments.of("()[]{}", true),
            Arguments.of("(]", false),
            Arguments.of("([])", true),
            Arguments.of("]", false),
        )
    }
}
