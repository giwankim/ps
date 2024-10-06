package leetcode

import java.util.ArrayDeque
import java.util.Deque

class SlidingWindowMaximumKt {
    fun maxSlidingWindow(
        nums: IntArray,
        k: Int,
    ): IntArray {
        val result: MutableList<Int> = mutableListOf()
        val deque: Deque<Int> = ArrayDeque()
        for (i in nums.indices) {
            while (deque.isNotEmpty() && deque.peek() <= i - k) {
                deque.poll()
            }
            while (deque.isNotEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast()
            }
            deque.add(i)
            if (i >= k - 1) {
                result.add(nums[deque.peek()])
            }
        }
        return result.toIntArray()
    }
}
