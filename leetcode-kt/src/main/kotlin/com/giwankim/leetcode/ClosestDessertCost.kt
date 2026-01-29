package com.giwankim.leetcode

import kotlin.math.abs
import kotlin.math.min

class ClosestDessertCost {
    fun closestCost(
        baseCosts: IntArray,
        toppingCosts: IntArray,
        target: Int,
    ): Int {
        var result: Int = -10_000
        for (baseCost in baseCosts) {
            var totalCost = chooseToppings(i = 0, cost = baseCost, toppingCosts = toppingCosts, target = target)
            result = closerToTarget(totalCost, result, target)
        }
        return result
    }

    private fun chooseToppings(
        i: Int,
        cost: Int,
        toppingCosts: IntArray,
        target: Int,
    ): Int {
        if (i == toppingCosts.size) {
            return cost
        }
        var result = cost
        for (numberOfToppings in 0..2) {
            val totalCost = chooseToppings(i + 1, cost + toppingCosts[i] * numberOfToppings, toppingCosts, target)
            result = closerToTarget(totalCost, result, target)
        }
        return result
    }

    private fun closerToTarget(
        a: Int,
        b: Int,
        target: Int,
    ): Int {
        val diff = abs(a - target) - abs(b - target)
        return when {
            diff == 0 -> min(a, b)
            diff < 0 -> a
            else -> b
        }
    }
}
