package aoc.dec2024

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readInput(name: String): List<String> = Path("src/main/kotlin/aoc/dec2024/input/$name.txt").readLines()

fun Any?.println() = println(this)
