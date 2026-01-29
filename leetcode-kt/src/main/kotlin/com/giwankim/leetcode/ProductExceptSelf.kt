package com.giwankim.leetcode

class ProductExceptSelf {
    fun productExceptSelf(nums: IntArray): IntArray {
        val products = IntArray(nums.size)
        var prefix = 1
        for (i in nums.indices) {
            products[i] = prefix
            prefix *= nums[i]
        }
        var suffix = 1
        for (i in nums.indices.reversed()) {
            products[i] *= suffix
            suffix *= nums[i]
        }
        return products
    }
}
