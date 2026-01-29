package com.giwankim.leetcode

class FirstMissingPositive {
    fun firstMissingPositive(nums: IntArray): Int {
        for (i in nums.indices) {
            while (nums[i] in 1..nums.size && nums[i] != nums[nums[i] - 1]) {
                nums.swap(i, nums[i] - 1)
            }
        }
        for (i in nums.indices) {
            if (nums[i] != i + 1) {
                return i + 1
            }
        }
        return nums.size + 1
    }

    fun IntArray.swap(
        i: Int,
        j: Int,
    ) {
        val temp = this[i]
        this[i] = this[j]
        this[j] = temp
    }
}
