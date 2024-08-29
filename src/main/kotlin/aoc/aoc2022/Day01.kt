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
): Int {
    fun findTopN(
        n: Int,
        elements: List<Int>,
    ): List<Int> {
        if (elements.size == n) {
            return elements
        }
        val x = elements.random()
        val small = elements.filter { it < x }
        val equal = elements.filter { it == x }
        val big = elements.filter { it > x }
        return when {
            big.size >= n -> findTopN(n, big)
            big.size + equal.size >= n -> (big + equal).take(n)
            else -> findTopN(n - big.size - equal.size, small) + equal + big
        }
    }
    return findTopN(n, data.map { it.sum() }).sum()
}

private fun parseInput(input: String) = input.split("\n\n").map { elf -> elf.lines().map { it.toInt() } }

fun main() {
    val input = File("src/main/kotlin/aoc/aoc2022/Day01.txt").readText()
    part1(input).println()
    part2(input).println()
}
