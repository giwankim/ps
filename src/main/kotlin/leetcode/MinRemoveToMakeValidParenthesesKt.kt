package leetcode

import java.util.ArrayDeque
import java.util.Deque

class MinRemoveToMakeValidParenthesesKt {
    fun minRemoveToMakeValid(s: String): String {
        val stack: Deque<Int> = ArrayDeque()
        val toRemove = BooleanArray(s.length)

        s.forEachIndexed { i, c ->
            when (c) {
                '(' -> stack.push(i)
                ')' -> {
                    if (stack.isNotEmpty()) {
                        stack.pop()
                    } else {
                        toRemove[i] = true
                    }
                }
            }
        }

        while (stack.isNotEmpty()) {
            toRemove[stack.pop()] = true
        }

        return buildString {
            s.forEachIndexed { i, c ->
                if (!toRemove[i]) {
                    append(c)
                }
            }
        }
    }
}
