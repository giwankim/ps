package leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class MinRemoveToMakeValidParenthesesKtTest :
    FunSpec(
        {
            test("minRemoveToMakeValid") {
                val sut = MinRemoveToMakeValidParenthesesKt()
                arguments.forAll { (s, expected) ->
                    sut.minRemoveToMakeValid(s) shouldBe expected
                }
            }
        },
    ) {
    companion object {
        val arguments =
            listOf(
                "lee(t(c)o)de)" to "lee(t(c)o)de",
                "a)b(c)d" to "ab(c)d",
                "))((" to "",
            )
    }
}
