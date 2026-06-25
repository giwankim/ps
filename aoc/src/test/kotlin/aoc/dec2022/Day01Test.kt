package aoc.dec2022

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day01Test {
    @Test
    fun part1() {
        val input =
            """
            1000
            2000
            3000

            4000

            5000
            6000

            7000
            8000
            9000

            10000
            """.trimIndent()
        assertThat(part1(input)).isEqualTo(24_000)
    }

    @Test
    fun part2() {
        val input =
            """
            1000
            2000
            3000

            4000

            5000
            6000

            7000
            8000
            9000

            10000
            """.trimIndent()
        assertThat(part2(input)).isEqualTo(45_000)
    }
}
