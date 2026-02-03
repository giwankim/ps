package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class CombinationSumTest :
    FunSpec(
        {
            lateinit var sut: CombinationSum

            beforeTest {
                sut = CombinationSum()
            }

            test("can't achieve target with candidates") {
                val candidates = intArrayOf(2)
                val target = 1
                val expected: List<List<Int>> = emptyList()
                sut.combinationSum(candidates, target) shouldBe expected
            }

            test("single candidate is the target") {
                val candidates = intArrayOf(1)
                val target = 1
                val expected = listOf(listOf(1))
                sut.combinationSum(candidates, target) shouldBe expected
            }

            context("general case") {
                withTests(
                    nameFn = { (candidates, target, expected) ->
                        "candidates=${candidates.contentToString()}, target=$target, expected=$expected"
                    },
                    CombinationSumTestCase(intArrayOf(2, 3, 6, 7), 7, listOf(listOf(2, 2, 3), listOf(7))),
                    CombinationSumTestCase(intArrayOf(2, 3, 5), 8, listOf(listOf(2, 2, 2, 2), listOf(2, 3, 3), listOf(3, 5))),
                ) { (candidates, target, expected) ->
                    sut.combinationSum(candidates, target) shouldBe expected
                }
            }
        },
    )

data class CombinationSumTestCase(
    val candidates: IntArray,
    val target: Int,
    val expected: List<List<Int>>,
)
