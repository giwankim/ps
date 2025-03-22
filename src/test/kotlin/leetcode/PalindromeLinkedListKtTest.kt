package leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class PalindromeLinkedListKtTest {
    @ParameterizedTest
    @MethodSource
    fun isPalindrome(
        head: ListNode?,
        expected: Boolean,
    ) {
        val actual = PalindromeLinkedListKt().isPalindrome(head)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun isPalindrome(): List<Arguments> =
            listOf(
                Arguments.of(ListNode.of(1, 2, 2, 1), true),
                Arguments.of(ListNode.of(1, 2), false),
            )
    }
}
