package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class ReverseStringTest :
    FunSpec({
        val sut = ReverseString()

        context("reverse string") {
            withTests(
                nameFn = { (s, expected) ->
                    "s=${s.concatToString()}, expected=${expected.concatToString()}"
                },
                ReverseStringCase(charArrayOf('h', 'e', 'l', 'l', 'o'), charArrayOf('o', 'l', 'l', 'e', 'h')),
                ReverseStringCase(charArrayOf('H', 'a', 'n', 'n', 'a', 'h'), charArrayOf('h', 'a', 'n', 'n', 'a', 'H')),
            ) { (s, expected) ->
                sut.reverseString(s)
                s.toList() shouldBe expected.toList()
            }
        }
    })

private data class ReverseStringCase(
    val s: CharArray,
    val expected: CharArray,
)
