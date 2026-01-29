package com.giwankim.leetcode

class SimplifyPath {
    fun simplifyPath(path: String): String {
        val stack = ArrayDeque<String>()
        val components = path.split("/")

        components.forEach {
            when (it) {
                "", "." -> {}
                ".." -> {
                    if (stack.isNotEmpty()) {
                        stack.removeLast()
                    }
                }
                else -> stack.addLast(it)
            }
        }

        val result =
            buildString {
                stack.forEach { append("/$it") }
            }
        return if (result.isNotEmpty()) result else "/"
    }
}
