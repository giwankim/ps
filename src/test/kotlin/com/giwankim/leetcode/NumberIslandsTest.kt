package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class NumberIslandsTest {
    @ParameterizedTest
    @MethodSource
    fun numIslands(
        grid: Array<CharArray>,
        expected: Int,
    ) {
        val actual = NumberIslands().numIslands(grid)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun numIslands(): List<Arguments> =
            listOf(
                Arguments.of(
                    arrayOf(
                        charArrayOf('1', '1', '1', '1', '0'),
                        charArrayOf('1', '1', '0', '1', '0'),
                        charArrayOf('1', '1', '0', '0', '0'),
                        charArrayOf('0', '0', '0', '0', '0'),
                    ),
                    1,
                ),
                Arguments.of(
                    arrayOf(
                        charArrayOf('1', '1', '0', '0', '0'),
                        charArrayOf('1', '1', '0', '0', '0'),
                        charArrayOf('0', '0', '1', '0', '0'),
                        charArrayOf('0', '0', '0', '1', '1'),
                    ),
                    3,
                ),
            )
    }
}
