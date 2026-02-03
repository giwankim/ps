package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class NumberOfProvincesTest :
    FunSpec(
        {
            lateinit var sut: NumberOfProvinces

            beforeTest { sut = NumberOfProvinces() }

            context("number of provinces") {
                test("islands") {
                    val isConnected = arrayOf(intArrayOf(1, 0, 0), intArrayOf(0, 1, 0), intArrayOf(0, 0, 1))
                    sut.findCircleNum(isConnected) shouldBe 3
                }

                test("two provinces") {
                    val isConnected = arrayOf(intArrayOf(1, 1, 0), intArrayOf(1, 1, 0), intArrayOf(0, 0, 1))
                    sut.findCircleNum(isConnected) shouldBe 2
                }
            }
        },
    )
