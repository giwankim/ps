package aoc.dec2024.day01

import aoc.dec2024.readInput
import kotlin.math.abs

fun part1(lines: List<String>): Long {
    val (left, right) = parse(lines)
    return left
        .sorted()
        .zip(right.sorted())
        .sumOf { (first, second) -> abs(first - second) }
}

fun part2(lines: List<String>): Long {
    val (left, right) = parse(lines)
    val frequencies = right.groupingBy { it }.eachCount()
    return left.sumOf { it * (frequencies[it] ?: 0) }
}

private fun parse(lines: List<String>): Pair<List<Long>, List<Long>> = lines
    .map { line ->
        line.split("""\s+""".toRegex()).let {
            check(it.size == 2) { "Line is malformed" }
            it[0].toLong() to it[1].toLong()
        }
    }.unzip()

fun main() {
    val lines = readInput("Day01")
    println(part1(lines))
    println(part2(lines))
}
