package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class ReverseLinkedListTest :
    FunSpec({
        val sut = ReverseLinkedList()

        context("reverse linked list") {
            withTests(
                nameFn = { (head, expected) -> "head=$head, expected=$expected" },
                ReverseLinkedListCase(ListNode.of(1, 2, 3, 4, 5), ListNode.of(5, 4, 3, 2, 1)),
                ReverseLinkedListCase(ListNode.of(1, 2), ListNode.of(2, 1)),
            ) { (head, expected) ->
                sut.reverseList(head) shouldBe expected
            }
        }
    })

private data class ReverseLinkedListCase(
    val head: ListNode?,
    val expected: ListNode?,
)
