package aoc.dec2024

import kotlin.math.abs

fun part1(lines: List<String>): Int {
    val (left, right) =
        lines
            .map { line ->
                val first = line.substringBefore(" ").toInt()
                val second = line.substringAfterLast(" ").toInt()
                first to second
            }.unzip()
    return left
        .sorted()
        .zip(right.sorted())
        .sumOf { (first, second) -> abs(first - second) }
}

fun part2(lines: List<String>): Int {
    //
    return -1
}

fun main() {
    val lines = readInput("Day01")
    println(part1(lines))
}
