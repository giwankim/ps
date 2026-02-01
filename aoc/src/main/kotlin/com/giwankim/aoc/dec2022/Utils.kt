package com.giwankim.aoc.dec2022

import java.math.BigInteger
import java.security.MessageDigest
private fun readResourceText(path: String): String {
    val resource = object {}.javaClass.getResource(path) ?: error("Resource not found: $path")
    return resource.readText().trimEnd()
}

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = readResourceText("/aoc/dec2022/$name.txt").lines()

fun readInputText(name: String): String = readResourceText("/aoc/dec2022/$name.txt")

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

/**
 * Cleaner shorthand for printing output.
 */
fun Any?.println(): Unit = println(this)
