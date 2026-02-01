package com.giwankim.aoc.dec2024

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day01Test {
    @Test
    fun part1() {
        val lines =
            """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """.trimIndent().lines()
        part1(lines) shouldBe 11
    }

    @Test
    fun part2() {
        val lines =
            """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """.trimIndent()
                .lines()
        part2(lines) shouldBe 31
    }
}
