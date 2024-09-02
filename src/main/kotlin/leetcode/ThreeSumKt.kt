package leetcode

import java.util.Arrays

class ThreeSumKt {
    fun threeSum(nums: IntArray): List<List<Int>> {
        val result: MutableList<List<Int>> = mutableListOf()
        Arrays.sort(nums)

        for (i in 0 until nums.size) {
            if (i > 0 && nums[i - 1] == nums[i]) {
                continue
            }
            var lo: Int = i + 1
            var hi: Int = nums.size - 1
            while (lo < hi) {
                val sum: Int = nums[i] + nums[lo] + nums[hi]
                if (sum < 0) {
                    lo += 1
                } else if (sum > 0) {
                    hi -= 1
                } else {
                    result.add(listOf(nums[i], nums[lo], nums[hi]))
                    while (lo < hi && nums[lo] == nums[lo + 1]) {
                        lo += 1
                    }
                    while (lo < hi && nums[hi] == nums[hi - 1]) {
                        hi -= 1
                    }
                    lo += 1
                    hi -= 1
                }
            }
        }

        return result
    }
}
