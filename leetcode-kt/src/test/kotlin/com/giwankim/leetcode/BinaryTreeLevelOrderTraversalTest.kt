package com.giwankim.leetcode

import com.giwankim.leetcode.support.TreeNode
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class BinaryTreeLevelOrderTraversalTest :
    FunSpec(
        {
            val sut = BinaryTreeLevelOrderTraversal()

            context("binary tree level order traversal") {
                withTests(
                    nameFn = { (root, expected) -> "root=${root?.prettyPrint()}, expected=$expected" },
                    BinaryTreeLevelOrderTraversalTestCase(
                        TreeNode.from(3, 9, 20, null, null, 15, 7),
                        listOf(listOf(3), listOf(9, 20), listOf(15, 7)),
                    ),
                    BinaryTreeLevelOrderTraversalTestCase(
                        TreeNode.from(1),
                        listOf(listOf(1)),
                    ),
                    BinaryTreeLevelOrderTraversalTestCase(
                        TreeNode.from(),
                        emptyList(),
                    ),
                ) { (root, expected) ->
                    sut.levelOrder(root) shouldBe expected
                }
            }
        },
    )

data class BinaryTreeLevelOrderTraversalTestCase(
    val root: TreeNode?,
    val expected: List<List<Int>>,
)
