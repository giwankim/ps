package com.giwankim.leetcode

class JumpGame {
    fun canJump(nums: IntArray): Boolean {
        var maxReach = 0
        for ((i, num) in nums.withIndex()) {
            if (i > maxReach) {
                return false
            }
            maxReach = maxOf(maxReach, i + num)
        }
        return maxReach + 1 >= nums.size
    }
}
