package com.giwankim.leetcode

import com.giwankim.leetcode.support.TreeNode
import java.util.LinkedList
import java.util.Queue

class BinaryTreeLevelOrderTraversal {
    fun levelOrder(root: TreeNode?): List<List<Int>> {
        if (root == null) {
            return emptyList()
        }
        val result = mutableListOf<List<Int>>()
        val queue: Queue<TreeNode> = LinkedList<TreeNode>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val level = mutableListOf<Int>()
            val size = queue.size
            for (i in 0 until size) {
                val node = queue.poll()
                level.add(node.`val`)
                if (node.left != null) {
                    queue.add(node.left)
                }
                if (node.right != null) {
                    queue.add(node.right)
                }
            }
            result.add(level)
        }
        return result
    }
}
