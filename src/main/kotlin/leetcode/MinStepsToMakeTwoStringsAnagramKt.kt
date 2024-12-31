package leetcode

class MinStepsToMakeTwoStringsAnagramKt {
    fun minSteps(
        s: String,
        t: String,
    ): Int {
        var result = 0
        val counts = IntArray(26)
        for (c in t) {
            counts[c - 'a'] += 1
        }
        for (c in s) {
            counts[c - 'a'] -= 1
        }
        for (count in counts) {
            if (count > 0) {
                result += count
            }
        }
        return result
    }
}
