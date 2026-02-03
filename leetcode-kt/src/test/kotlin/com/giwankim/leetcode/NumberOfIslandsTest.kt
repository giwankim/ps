package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.shouldBe

class NumberOfIslandsTest :
    FunSpec({
        val sut = NumberOfIslands()

        context("number of islands") {
            withTests(
                nameFn = { (grid, expected) ->
                    "grid=${grid.contentDeepToString()}, expected=$expected"
                },
                NumberOfIslandsCase(
                    arrayOf(
                        charArrayOf('1', '1', '1', '1', '0'),
                        charArrayOf('1', '1', '0', '1', '0'),
                        charArrayOf('1', '1', '0', '0', '0'),
                        charArrayOf('0', '0', '0', '0', '0'),
                    ),
                    1,
                ),
                NumberOfIslandsCase(
                    arrayOf(
                        charArrayOf('1', '1', '0', '0', '0'),
                        charArrayOf('1', '1', '0', '0', '0'),
                        charArrayOf('0', '0', '1', '0', '0'),
                        charArrayOf('0', '0', '0', '1', '1'),
                    ),
                    3,
                ),
            ) { (grid, expected) ->
                sut.numIslands(grid) shouldBe expected
            }
        }
    })

private data class NumberOfIslandsCase(
    val grid: Array<CharArray>,
    val expected: Int,
)
