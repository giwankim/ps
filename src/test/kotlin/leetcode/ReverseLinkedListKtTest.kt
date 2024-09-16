package leetcode

import datatype.ListNode
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
        fun reverseList(): List<Arguments> =
            listOf(
                Arguments.of(ListNode.createList(1, 2, 3, 4, 5), ListNode.createList(5, 4, 3, 2, 1)),
                Arguments.of(ListNode.createList(1, 2), ListNode.createList(2, 1)),
            )
    }
}
