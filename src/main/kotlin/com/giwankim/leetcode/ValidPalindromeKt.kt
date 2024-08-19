package com.giwankim.leetcode

class ValidPalindromeKt {
    fun isPalindrome(s: String): Boolean {
        var start = 0
        var end = s.length - 1
        while (start < end) {
            when {
                !Character.isLetterOrDigit(s[start]) -> start += 1
                !Character.isLetterOrDigit(s[end]) -> end -= 1
                Character.toLowerCase(s[start]) != Character.toLowerCase(s[end]) -> return false
                else -> {
                    start += 1
                    end -= 1
                }
            }
        }
        return true
    }
}
