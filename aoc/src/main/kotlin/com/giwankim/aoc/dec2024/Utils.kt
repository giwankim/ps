package com.giwankim.aoc.dec2024

private fun readResourceLines(path: String): List<String> {
    val resource = object {}.javaClass.getResource(path) ?: error("Resource not found: $path")
    return resource.readText().trimEnd().lines()
}

fun readInput(name: String): List<String> = readResourceLines("/aoc/dec2024/input/$name.txt")

fun Any?.println() = println(this)
