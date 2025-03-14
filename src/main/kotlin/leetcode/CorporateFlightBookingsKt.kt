package leetcode

class CorporateFlightBookingsKt {
    fun corpFlightBookings(
        bookings: Array<IntArray>,
        n: Int,
    ): IntArray {
        val result = IntArray(n)
        for (booking in bookings) {
            val first = booking[0] - 1
            val last = booking[1] - 1
            val seats = booking[2]
            for (i in first..last) {
                result[i] += seats
            }
        }
        return result
    }
}
