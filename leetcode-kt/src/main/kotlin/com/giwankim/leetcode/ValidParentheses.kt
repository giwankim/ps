package com.giwankim.leetcode

import java.util.ArrayDeque
import java.util.Deque

class ValidParentheses {
    fun isValid(s: String): Boolean {
        val map: Map<Char, Char> = mapOf(')' to '(', '}' to '{', ']' to '[')
        val stack: Deque<Char> = ArrayDeque()
        for (c in s) {
            if (!map.containsKey(c)) {
                stack.push(c)
            } else if (stack.isEmpty() || stack.pop() != map[c]) {
                return false
            }
        }
        return stack.isEmpty()
    }
}
