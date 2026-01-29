package com.giwankim.leetcode

class JumpGameII {
    fun jump(nums: IntArray): Int {
        var result = 0
        var farthest = 0
        var end = 0
        for (i in 0 until nums.size - 1) {
            farthest = maxOf(farthest, i + nums[i])
            if (i == end) {
                result += 1
                end = farthest
            }
        }
        return result
    }
}
