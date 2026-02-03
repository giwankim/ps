package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class MaximumUnitsOnATruckTest :
    FunSpec({
        val sut = MaximumUnitsOnATruck()

        context("maximum units on a truck") {
            withTests(
                nameFn = { (boxTypes, truckSize, expected) ->
                    "boxTypes=${boxTypes.contentDeepToString()}, truckSize=$truckSize, expected=$expected"
                },
                MaximumUnitsOnATruckCase(
                    arrayOf(intArrayOf(1, 3), intArrayOf(2, 2), intArrayOf(3, 1)),
                    4,
                    8,
                ),
                MaximumUnitsOnATruckCase(
                    arrayOf(intArrayOf(5, 10), intArrayOf(2, 5), intArrayOf(4, 7), intArrayOf(3, 9)),
                    10,
                    91,
                ),
            ) { (boxTypes, truckSize, expected) ->
                sut.maximumUnits(boxTypes, truckSize) shouldBe expected
            }
        }
    })

private data class MaximumUnitsOnATruckCase(
    val boxTypes: Array<IntArray>,
    val truckSize: Int,
    val expected: Int,
)
