package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RemoveNthFromEndKtTest :
    FunSpec(
        {
            lateinit var sut: RemoveNthFromEndKt

            beforeTest { sut = RemoveNthFromEndKt() }

            test("remove last node, then empty list") {
                val head = ListNode.of(1)
                val expected = ListNode.of()
                sut.removeNthFromEnd(head, 1) shouldBe expected
            }

            test("remove first node from length 2 list") {
                val head = ListNode.of(1, 2)
                val expected = ListNode.of(1)
                sut.removeNthFromEnd(head, 1) shouldBe expected
            }

            test("remove second to last node") {
                val head = ListNode.of(1, 2, 3, 4, 5)
                val expected = ListNode.of(1, 2, 3, 5)
                sut.removeNthFromEnd(head, 2) shouldBe expected
            }
        },
    )
