package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CorporateFlightBookingsTest :
    FunSpec(
        {
            lateinit var sut: CorporateFlightBookings

            beforeTest { sut = CorporateFlightBookings() }

            test("simple case") {
                val bookings = arrayOf(intArrayOf(1, 2, 10), intArrayOf(2, 2, 15))
                val actual = sut.corpFlightBookings(bookings, 2)
                actual.toList() shouldBe listOf(10, 25)
            }

            test("more general case") {
                val bookings = arrayOf(intArrayOf(1, 2, 10), intArrayOf(2, 3, 20), intArrayOf(2, 5, 25))
                val actual = sut.corpFlightBookings(bookings, 5)
                actual.toList() shouldBe listOf(10, 55, 45, 25, 25)
            }
        },
    )
