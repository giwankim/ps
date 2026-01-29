package com.giwankim.leetcode

class CorporateFlightBookings {
    fun corpFlightBookings(
        bookings: Array<IntArray>,
        n: Int,
    ): IntArray {
        val result = IntArray(n)
        for ((first, last, seats) in bookings) {
            result[first - 1] += seats
            if (last < n) {
                result[last] -= seats
            }
        }
        for (i in 1 until result.size) {
            result[i] += result[i - 1]
        }
        return result
    }
}
