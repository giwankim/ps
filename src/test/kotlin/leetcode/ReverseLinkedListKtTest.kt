package leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ReverseLinkedListKtTest {
    @ParameterizedTest
    @MethodSource
    fun reverseList(
        head: ListNode?,
        expected: ListNode?,
    ) {
        val actual = ReverseLinkedListKt().reverseList(head)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun reverseList(): List<Arguments> = listOf(
            Arguments.of(ListNode.of(1, 2, 3, 4, 5), ListNode.of(5, 4, 3, 2, 1)),
            Arguments.of(ListNode.of(1, 2), ListNode.of(2, 1)),
        )
    }
}
