package leetcode

class RemoveNthFromEndKt {
    fun removeNthFromEnd(
        head: ListNode?,
        n: Int,
    ): ListNode? {
        var dummy = ListNode(-1).apply { next = head }

        var fast = dummy
        repeat(n) { fast = fast.next!! }

        var slow = dummy
        while (fast.next != null) {
            fast = fast.next!!
            slow = slow.next!!
        }

        slow.next = slow.next?.next
        return dummy.next
    }
}
