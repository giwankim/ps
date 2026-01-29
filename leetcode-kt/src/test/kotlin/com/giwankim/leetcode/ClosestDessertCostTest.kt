package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ClosestDessertCostTest :
    FunSpec(
        {
            lateinit var sut: ClosestDessertCost

            beforeTest { sut = ClosestDessertCost() }

            test("can make target cost") {
                sut.closestCost(
                    baseCosts = intArrayOf(1, 7),
                    toppingCosts = intArrayOf(3, 4),
                    target = 10,
                ) shouldBe 10
            }

            test("if can't make target cost, then return the closest") {
                sut.closestCost(
                    baseCosts = intArrayOf(2, 3),
                    toppingCosts = intArrayOf(4, 5, 100),
                    target = 18,
                ) shouldBe 17
            }

            test("multiple close values, then return the lower one") {
                sut.closestCost(
                    baseCosts = intArrayOf(3, 10),
                    toppingCosts = intArrayOf(2, 5),
                    target = 9,
                ) shouldBe 8
            }

            test("singleton base cost is above target") {
                sut.closestCost(baseCosts = intArrayOf(10), toppingCosts = intArrayOf(1), target = 1) shouldBe 10
            }
        },
    )
