package aoc.dec2024.day02

import aoc.dec2024.println
import aoc.dec2024.readInput
import kotlin.math.absoluteValue

fun part1(lines: List<String>): Int {
    var result = 0
    for (line in lines) {
        val report = line.split("\\s+".toRegex()).map { it.toInt() }
        if (isReportSafe(report)) {
            result += 1
        }
    }
    return result
}

private fun isReportSafe(report: List<Int>): Boolean {
    var isDiff = true
    var isAscending = true
    var isDescending = true
    for (i in 0..<report.lastIndex) {
        val a = report[i]
        val b = report[i + 1]
        val diff = (a - b).absoluteValue
        isDiff = isDiff && 1 <= diff && diff <= 3
        when {
            a < b -> isDescending = false
            a > b -> isAscending = false
            else -> {
                isAscending = false
                isDescending = false
            }
        }
    }
    return isDiff && (isAscending || isDescending)
}

fun main() {
    val lines = readInput("Day02")
    part1(lines).println()
}
