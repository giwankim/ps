package aoc.aoc2022

import java.io.File

fun part1(input: String): Int {
    val data = parseInput(input)
    return topNElves(data, 1)
}

fun part2(input: String): Int {
    val data = parseInput(input)
    return topNElves(data, 3)
}

private fun topNElves(
    data: List<List<Int>>,
    n: Int,
): Int =
    data
        .map { it.sum() }
        .sortedDescending()
        .take(n)
        .sum()

private fun parseInput(input: String) = input.split("\n\n").map { elf -> elf.lines().map { it.toInt() } }

fun main() {
    val input = File("src/main/kotlin/aoc/aoc2022/Day01.txt").readText()
    part1(input).println()
    part2(input).println()
}
