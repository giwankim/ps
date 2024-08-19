package com.giwankim.leetcode

class ReverseStringKt {
    fun reverseString(s: CharArray) {
        var start = 0
        var end = s.size - 1
        while (start < end) {
            s[start] = s[end].also { s[end] = s[start] }
            start += 1
            end -= 1
        }
    }
}
