package leetcode

class DecodeWays {
    fun numDecodings(s: String): Int {
        val set: Set<String> = (1..26).map { it.toString() }.toSet()

        val a = MutableList(s.length + 1) { 0 }
        a[0] = 1

        for (i in 1..s.length) {
            if (set.contains(s.substring(i - 1, i))) {
                a[i] += a[i - 1]
            }
            if (i > 1 && set.contains(s.substring(i - 2, i))) {
                a[i] += a[i - 2]
            }
        }
        return a.last()
    }
}
