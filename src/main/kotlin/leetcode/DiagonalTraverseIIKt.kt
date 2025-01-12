package leetcode

class DiagonalTraverseIIKt {
    fun findDiagonalOrder(nums: List<List<Int>>): IntArray {
        val result = mutableListOf<Int>()
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.addLast(0 to 0)
        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            result.add(nums[x][y])
            if (y == 0 && x + 1 < nums.size) {
                queue.addLast(x + 1 to y)
            }
            if (y + 1 < nums[x].size) {
                queue.addLast(x to y + 1)
            }
        }
        return result.toIntArray()
    }
}
