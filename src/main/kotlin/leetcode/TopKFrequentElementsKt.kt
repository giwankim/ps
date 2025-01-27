package leetcode

import java.util.PriorityQueue

class TopKFrequentElementsKt {
    fun topKFrequent(
        nums: IntArray,
        k: Int,
    ): IntArray {
        val counts = nums.toList().groupingBy { it }.eachCount()
        val pq = PriorityQueue<Pair<Int, Int>> { a, b -> b.second - a.second }
        pq.addAll(counts.map { it.key to it.value })

        val result = mutableListOf<Int>()
        while (pq.isNotEmpty() && result.size < k) {
            result.add(pq.poll().first)
        }
        return result.toIntArray()
    }
}
