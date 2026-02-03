package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class BestTimeToBuyAndSellStockIITest :
    FunSpec({
        val sut = BestTimeToBuyAndSellStockII()

        context("best time to buy and sell stock ii") {
            withTests(
                nameFn = { (prices, expected) -> "prices=${prices.contentToString()}, expected=$expected" },
                BestTimeToBuyAndSellStockIICase(intArrayOf(7, 1, 5, 3, 6, 4), 7),
                BestTimeToBuyAndSellStockIICase(intArrayOf(1, 2, 3, 4, 5), 4),
                BestTimeToBuyAndSellStockIICase(intArrayOf(7, 6, 4, 3, 1), 0),
            ) { (prices, expected) ->
                sut.maxProfit(prices) shouldBe expected
            }
        }
    })

private data class BestTimeToBuyAndSellStockIICase(
    val prices: IntArray,
    val expected: Int,
)
