package com.giwankim.leetcode

class UniquePaths {
    fun uniquePaths(
        m: Int,
        n: Int,
    ): Int {
        val a = Array(m) { IntArray(n) { 1 } }
        for (i in 1 until m) {
            for (j in 1 until n) {
                a[i][j] = a[i - 1][j] + a[i][j - 1]
            }
        }
        return a[m - 1][n - 1]
    }
}
