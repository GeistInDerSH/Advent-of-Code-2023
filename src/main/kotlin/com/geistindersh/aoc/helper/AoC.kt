package com.geistindersh.aoc.helper

import kotlin.time.Duration
import kotlin.time.measureTime

interface AoC<T, V> {
    fun part1(): T

    fun part2(): V
}

/**
 * Standardized reporting function to avoid code duplication
 *
 * @param year The year for the problem
 * @param dayNumber The day number for the problem
 */
@Suppress("unused")
fun <T, V> AoC<T, V>.report(
    year: Int,
    dayNumber: Int,
) = report(year, dayNumber, this.part1(), this.part2())

/**
 * Common code to benchmark the runtime of a specific day of Advent of Code
 *
 * @param warmup The number of runs to do before measuring the time. Set to 0 to avoid warming up the JIT
 * @param count The number of runs to preform
 * @param verbose If the times and values for each of the parts should be printed to standard out
 *
 * @return The average duration of the part1 & part2 across [count] number of runs
 */
@Suppress("unused")
fun <T, V> AoC<T, V>.benchmark(
    warmup: Int = 10,
    count: Int = 10,
    verbose: Boolean = false,
): Duration {
    repeat(warmup) {
        this.part1()
        this.part2()
    }

    var totalTime = Duration.ZERO
    repeat(count) {
        var p1: T
        val p1Time = measureTime { p1 = this.part1() }

        var p2: V
        val p2Time = measureTime { p2 = this.part2() }

        totalTime += p1Time + p2Time

        if (verbose) {
            println("Part 1: $p1 ($p1Time)\tp2: $p2 ($p2Time)")
        }
    }
    return totalTime / count
}

/**
 * Common code to benchmark the runtime of a specific day of Advent of Code
 * without warming up the JIT compiler.
 *
 * @param verbose If the times and values for each of the parts should be printed to standard out
 *
 * @return The average duration of the part1 and part2 for a single run
 */
@Suppress("unused")
fun <T, V> AoC<T, V>.benchmarkColdStart(verbose: Boolean = false) = this.benchmark(0, 1, verbose)
