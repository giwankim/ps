package com.giwankim.ch07

import java.util.Random

fun main() {
    fun rand(): Int? = Random().nextInt(100 - 1) + 1

    val x = rand() ?: 0
    println("x: $x")
}
