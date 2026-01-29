package com.giwankim.leetcode

import kotlin.math.max

class MergeIntervals {
    fun merge(intervals: Array<IntArray>): Array<IntArray> {
        val result = mutableListOf<IntArray>()

        intervals.sortWith(compareBy({ it[0] }, { it[1] }))
        var start = intervals[0][0]
        var end = intervals[0][1]

        for (i in 1 until intervals.size) {
            if (end < intervals[i][0]) {
                result.add(intArrayOf(start, end))
                start = intervals[i][0]
                end = intervals[i][1]
            } else {
                end = max(end, intervals[i][1])
            }
        }
        result.add(intArrayOf(start, end))
        return result.toTypedArray()
    }
}
