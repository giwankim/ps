package leetcode

class LongestIncreasingSubsequenceKt {
    fun lengthOfLIS(nums: IntArray): Int {
        val piles = mutableListOf<Int>()
        for (num in nums) {
            var index = piles.binarySearch(num)
            if (index < 0) {
                index = index.inv()
            }
            if (index == piles.size) {
                piles.add(num)
            } else {
                piles[index] = num
            }
        }
        return piles.size
    }
}
