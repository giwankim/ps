package leetcode

class CountBinarySubstringsKt {
    fun countBinarySubstrings(s: String): Int {
        var result = 0
        var cur = s[0]
        var len = 0
        var prevLen = 0
        for (c in s) {
            if (c == cur) {
                len += 1
                continue
            }
            result += minOf(len, prevLen)
            prevLen = len
            len = 1
            cur = c
        }
        return result + minOf(len, prevLen)
    }
}
