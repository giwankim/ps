package aoc.dec2024

import kotlin.io.path.Path
import kotlin.io.path.readText

fun readInput(name: String): List<String> = Path("src/main/kotlin/aoc/dec2024/$name.txt").readText().trim().lines()

fun Any?.println() = println(this)
