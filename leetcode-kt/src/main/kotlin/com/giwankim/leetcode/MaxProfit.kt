package com.giwankim.leetcode

class MaxProfit {
    fun maxProfit(prices: IntArray): Int {
        var maxProfit = 0
        var minPrice = Int.MAX_VALUE
        for (price in prices) {
            maxProfit = maxProfit.coerceAtLeast(price - minPrice)
            minPrice = minPrice.coerceAtMost(price)
        }
        return maxProfit
    }
}
