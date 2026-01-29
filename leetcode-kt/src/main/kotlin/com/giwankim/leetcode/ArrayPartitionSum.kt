package com.giwankim.leetcode

class ArrayPartitionSum {
    fun arrayPairSum(nums: IntArray): Int {
        nums.sort()
        var ans = 0
        for ((i, n) in nums.withIndex()) {
            if (i % 2 == 0) {
                ans += n
            }
        }
        return ans
    }
}
