package leetcode

class ListNode(
    var `val`: Int,
) {
    var next: ListNode? = null

    companion object {
        fun of(vararg values: Int): ListNode? {
            val head = ListNode(-1)
            var tail: ListNode? = head
            values.forEach { value ->
                tail?.next = ListNode(value)
                tail = tail?.next
            }
            return head.next
        }
    }
}
