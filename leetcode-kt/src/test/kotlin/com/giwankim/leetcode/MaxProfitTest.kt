package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class MaxProfitTest :
    FunSpec({
        val sut = MaxProfit()

        context("max profit") {
            withTests(
                nameFn = { (prices, expected) -> "prices=${prices.contentToString()}, expected=$expected" },
                MaxProfitCase(intArrayOf(7, 1, 5, 3, 6, 4), 5),
                MaxProfitCase(intArrayOf(7, 6, 4, 3, 1), 0),
            ) { (prices, expected) ->
                sut.maxProfit(prices) shouldBe expected
            }
        }
    })

private data class MaxProfitCase(
    val prices: IntArray,
    val expected: Int,
)
