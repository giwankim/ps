package com.giwankim.leetcode

class BestTimeToBuyAndSellStockII {
    fun maxProfit(prices: IntArray): Int {
        var result = 0
        for (i in 1 until prices.size) {
            val diff = prices[i] - prices[i - 1]
            if (diff > 0) {
                result += diff
            }
        }
        return result
    }
}
