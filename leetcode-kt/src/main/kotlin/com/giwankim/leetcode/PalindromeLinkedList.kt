package com.giwankim.leetcode

import java.util.Deque
import java.util.LinkedList

class PalindromeLinkedList {
    fun isPalindrome(head: ListNode?): Boolean {
        val deque: Deque<Int> = LinkedList()
        var node = head
        while (node != null) {
            deque.add(node.`val`)
            node = node.next
        }
        while (deque.size > 1) {
            if (deque.pollFirst() != deque.pollLast()) {
                return false
            }
        }
        return true
    }
}
