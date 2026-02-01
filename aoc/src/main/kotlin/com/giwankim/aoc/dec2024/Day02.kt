package com.giwankim.aoc.dec2024

import kotlin.math.absoluteValue

fun part1(reports: List<List<Int>>): Int {
    var result = 0
    for (report in reports) {
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

fun part1Functional(reports: List<List<Int>>): Int = reports.count(::isReportSafeFunctional)

private fun isReportSafeFunctional(report: List<Int>): Boolean {
    val differences = report.zipWithNext { a, b -> a - b }
    return differences.all { it in -3..3 } && (differences.all { it > 0 } || differences.all { it < 0 })
}

fun part2(reports: List<List<Int>>): Int {
    var result = 0
    for (report in reports) {
        var safe = false
        for (i in report.indices) {
            safe = isReportSafe(report.toMutableList().apply { removeAt(i) })
            if (safe) {
                break
            }
        }
        if (safe) {
            result += 1
        }
    }
    return result
}

fun part2Functional(reports: List<List<Int>>): Int = reports.count { report ->
    report.indices.any {
        val dampened = report.toMutableList().apply { removeAt(it) }.toList()
        isReportSafeFunctional(dampened)
    }
}

fun main() {
    val lines = readInput("Day02")
    val reports: List<List<Int>> = lines.map { line -> line.split("\\s+".toRegex()).map { it.toInt() } }

//    part1(reports).println()
    part1Functional(reports).println()
//    part2(reports).println()
    part2Functional(reports).println()
}
