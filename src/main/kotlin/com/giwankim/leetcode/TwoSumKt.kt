package com.giwankim.leetcode

class TwoSumKt {
    fun twoSum(
        nums: IntArray,
        target: Int,
    ): IntArray {
        val numToIndex: MutableMap<Int, Int> = mutableMapOf()
        for ((i, num) in nums.withIndex()) {
            if (numToIndex.containsKey(target - num)) {
                return intArrayOf(numToIndex.getValue(target - num), i)
            }
            numToIndex.put(num, i)
        }
        return intArrayOf(0, 0)
    }
}