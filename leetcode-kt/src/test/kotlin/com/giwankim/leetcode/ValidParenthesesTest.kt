package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class ValidParenthesesTest :
    FunSpec({
        val sut = ValidParentheses()

        context("valid parentheses") {
            withTests(
                nameFn = { (s, expected) -> "s=$s, expected=$expected" },
                ValidParenthesesCase("()", true),
                ValidParenthesesCase("()[]{}", true),
                ValidParenthesesCase("(]", false),
                ValidParenthesesCase("([])", true),
                ValidParenthesesCase("]", false),
            ) { (s, expected) ->
                sut.isValid(s) shouldBe expected
            }
        }
    })

private data class ValidParenthesesCase(
    val s: String,
    val expected: Boolean,
)
