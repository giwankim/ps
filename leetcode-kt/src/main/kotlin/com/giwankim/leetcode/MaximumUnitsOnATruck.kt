package com.giwankim.leetcode

class MaximumUnitsOnATruck {
    fun maximumUnits(
        boxTypes: Array<IntArray>,
        truckSize: Int,
    ): Int {
        var result = 0
        var size = truckSize
        boxTypes.sortByDescending { it[1] }
        for (boxType in boxTypes) {
            if (boxType[0] <= size) {
                result += boxType[0] * boxType[1]
                size -= boxType[0]
            } else {
                result += size * boxType[1]
                break
            }
        }
        return result
    }
}
