package leetcode

class TopKFrequentElementsKt {
    fun topKFrequent(
        nums: IntArray,
        k: Int,
    ): IntArray {
        val counts = nums.toList().groupingBy { it }.eachCount()
        val buckets = List<MutableList<Int>>(nums.size + 1) { mutableListOf<Int>() }
        counts.forEach { (num, count) -> buckets[count].add(num) }

        val result = mutableListOf<Int>()

        for (i in buckets.lastIndex downTo 0) {
            result.addAll(buckets[i])
            if (result.size >= k) {
                break
            }
        }

        return result.toIntArray()
    }
}
