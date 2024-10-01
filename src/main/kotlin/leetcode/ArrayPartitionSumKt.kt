package leetcode

class ArrayPartitionSumKt {
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
