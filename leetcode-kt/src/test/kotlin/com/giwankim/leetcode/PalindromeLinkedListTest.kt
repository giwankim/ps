package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class PalindromeLinkedListTest :
    FunSpec({
        val sut = PalindromeLinkedList()

        context("palindrome linked list") {
            withTests(
                nameFn = { (head, expected) -> "head=$head, expected=$expected" },
                PalindromeLinkedListCase(ListNode.of(1, 2, 2, 1), true),
                PalindromeLinkedListCase(ListNode.of(1, 2), false),
            ) { (head, expected) ->
                sut.isPalindrome(head) shouldBe expected
            }
        }
    })

private data class PalindromeLinkedListCase(
    val head: ListNode?,
    val expected: Boolean,
)
