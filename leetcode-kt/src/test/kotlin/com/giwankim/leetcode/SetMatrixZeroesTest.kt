package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SetMatrixZeroesTest :
    FunSpec(
        {
            lateinit var sut: SetMatrixZeroes

            beforeTest { sut = SetMatrixZeroes() }

            context("set matrix zeroes") {
                test("simple case") {
                    val matrix = arrayOf(intArrayOf(1, 1, 1), intArrayOf(1, 0, 1), intArrayOf(1, 1, 1))
                    val expected = arrayOf(intArrayOf(1, 0, 1), intArrayOf(0, 0, 0), intArrayOf(1, 0, 1))

                    sut.setZeroes(matrix)

                    matrix.map { it.toList() } shouldBe expected.map { it.toList() }
                }

                test("general case") {
                    val matrix = arrayOf(intArrayOf(0, 1, 2, 0), intArrayOf(3, 4, 5, 2), intArrayOf(1, 3, 1, 5))
                    val expected = arrayOf(intArrayOf(0, 0, 0, 0), intArrayOf(0, 4, 5, 0), intArrayOf(0, 3, 1, 0))

                    sut.setZeroes(matrix)

                    matrix.map { it.toList() } shouldBe expected.map { it.toList() }
                }
            }
        },
    )
