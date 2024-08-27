package leetcode

class LongestPalindromeKt {
    var left: Int = 0
    var maxLen: Int = 0

    fun longestPalindrome(s: String): String {
        if (s.length <= 1) {
            return s
        }
        for (i in 0 until s.length - 1) {
            extendPalindrome(s, i, i)
            extendPalindrome(s, i, i + 1)
        }
        return s.substring(left, left + maxLen)
    }

    fun extendPalindrome(
        s: String,
        j: Int,
        k: Int,
    ) {
        var l = j
        var r = k
        while (l >= 0 && r < s.length && s[l] == s[r]) {
            l -= 1
            r += 1
        }
        val len = r - l - 1
        if (len > maxLen) {
            left = l + 1
            maxLen = len
        }
    }
}
