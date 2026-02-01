package com.giwankim.aoc.dec2024

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.State
import org.openjdk.jmh.annotations.Scope

@State(Scope.Benchmark)
class Day01Benchmark {
    private val lines: List<String> = readInput("Day01")

    @Benchmark
    fun part1Benchmark(): Long = part1(lines)

    @Benchmark
    fun part2Benchmark(): Long = part2(lines)
}

/*
main summary:
Benchmark                       Mode  Cnt     Score     Error  Units
Day01Benchmark.part1Benchmark  thrpt    5  4408.477 ± 316.320  ops/s
Day01Benchmark.part2Benchmark  thrpt    5  7062.478 ± 172.070  ops/s
*/
