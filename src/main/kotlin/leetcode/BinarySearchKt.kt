package leetcode

class BinarySearchKt {
    fun search(
        nums: IntArray,
        target: Int,
    ): Int {
        var l = 0
        var r = nums.size - 1
        while (l <= r) {
            val mid = l + (r - l) / 2
            println("l = $l")
            println("r = $r")
            println("mid = $mid")
            when {
                nums[mid] == target -> return mid
                nums[mid] < target -> l = mid + 1
                else -> r = mid - 1
            }
        }
        return -1
    }
}
