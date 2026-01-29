package com.giwankim.leetcode.support

data class TreeNode(
    var `val`: Int,
    var left: TreeNode? = null,
    var right: TreeNode? = null,
) {
    constructor() : this(0)

    override fun toString(): String = `val`.toString()

    fun prettyPrint(): String {
        val sb = StringBuilder()
        printR(this, 0, sb)
        return sb.toString()
    }

    private fun printR(node: TreeNode?, level: Int, sb: StringBuilder) {
        if (node != null) {
            printR(node.right, level + 1, sb)
            sb.append("\t".repeat(level))
            sb.append(node.`val`)
            sb.append("\n")
            printR(node.left, level + 1, sb)
        }
    }

    companion object {
        fun from(vararg vals: Int?): TreeNode? {
            if (vals.isEmpty()) return null
            return TreeNode(vals[0]!!, constructFromArray(vals, 1), constructFromArray(vals, 2))
        }

        private fun constructFromArray(vals: Array<out Int?>, k: Int): TreeNode? {
            if (k < 0 || k >= vals.size) return null
            val value = vals[k] ?: return null
            return TreeNode(value).apply {
                left = constructFromArray(vals, 2 * k + 1)
                right = constructFromArray(vals, 2 * k + 2)
            }
        }
    }
}
