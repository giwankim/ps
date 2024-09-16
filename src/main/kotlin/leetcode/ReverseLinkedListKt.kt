package leetcode

import datatype.ListNode

class ReverseLinkedListKt {
    fun reverseList(head: ListNode?): ListNode? {
        var node: ListNode? = head
        var prev: ListNode? = null
        while (node != null) {
            val next = node.next
            node.next = prev
            prev = node
            node = next
        }
        return prev
    }
}
