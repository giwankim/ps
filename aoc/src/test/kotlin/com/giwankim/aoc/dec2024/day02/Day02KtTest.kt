package com.giwankim.aoc.dec2024.day02

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day02KtTest {
    @Test
    fun part1() {
        val lines =
            """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """.trimIndent()
                .lines()
        val reports = lines.map { it.split("\\s+".toRegex()).map { it.toInt() } }
    /*
      7 6 4 2 1: Safe because the levels are all decreasing by 1 or 2.
      1 2 7 8 9: Unsafe because 2 7 is an increase of 5.
      9 7 6 2 1: Unsafe because 6 2 is a decrease of 4.
      1 3 2 4 5: Unsafe because 1 3 is increasing but 3 2 is decreasing.
      8 6 4 4 1: Unsafe because 4 4 is neither an increase or a decrease.
      1 3 6 7 9: Safe because the levels are all increasing by 1, 2, or 3.
     */
        part1(reports) shouldBe 2
        part1Functional(reports) shouldBe 2
    }

    @Test
    fun part2() {
        val lines =
            """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """.trimIndent()
                .lines()
        val reports = lines.map { it.split("\\s+".toRegex()).map { it.toInt() } }
    /*
      7 6 4 2 1: Safe without removing any level.
      1 2 7 8 9: Unsafe regardless of which level is removed.
      9 7 6 2 1: Unsafe regardless of which level is removed.
      1 3 2 4 5: Safe by removing the second level, 3.
      8 6 4 4 1: Safe by removing the third level, 4.
      1 3 6 7 9: Safe without removing any level.
     */
        part2(reports) shouldBe 4
        part2Functional(reports) shouldBe 4
    }
}
