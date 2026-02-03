package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class LetterCombinationOfAPhoneNumberTest :
    FunSpec({
        val sut = LetterCombinationOfAPhoneNumber()

        context("letter combination of a phone number") {
            withTests(
                nameFn = { (digits, expected) -> "digits=$digits, expected=$expected" },
                LetterCombinationOfAPhoneNumberCase(
                    "23",
                    listOf("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"),
                ),
                LetterCombinationOfAPhoneNumberCase("", emptyList()),
                LetterCombinationOfAPhoneNumberCase("2", listOf("a", "b", "c")),
            ) { (digits, expected) ->
                sut.letterCombinations(digits) shouldBe expected
            }
        }
    })

private data class LetterCombinationOfAPhoneNumberCase(
    val digits: String,
    val expected: List<String>,
)
