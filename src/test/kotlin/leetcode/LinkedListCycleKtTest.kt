package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LinkedListCycleKtTest :
    FunSpec(
        {
            val sut = LinkedListCycleKt()

            test("Single element list should return false") {
                val head = ListNode(1)
                sut.hasCycle(head) shouldBe false
            }

            test("length two linked list has cycle") {
                val head = ListNode.of(1, 2)
                head?.next?.next = head
                sut.hasCycle(head) shouldBe true
            }

            test("length four linked list has cycle") {
                val head = ListNode.of(3, 2, 0, 4)
                head
                    ?.next
                    ?.next
                    ?.next
                    ?.next = head.next
                sut.hasCycle(head) shouldBe true
            }
        },
    )
