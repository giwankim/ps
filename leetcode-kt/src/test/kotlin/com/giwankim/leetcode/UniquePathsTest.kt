package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class UniquePathsTest :
    FunSpec(
        {
            val sut = UniquePaths()

            context("unique paths") {
                withData(
                    UniquePathTestCase(3, 7, 28),
                    UniquePathTestCase(3, 2, 3),
                ) { (m, n, expected) -> sut.uniquePaths(m, n) shouldBe expected }
            }
        },
    )

data class UniquePathTestCase(
    val m: Int,
    val n: Int,
    val expected: Int,
)
