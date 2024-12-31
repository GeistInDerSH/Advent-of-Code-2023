package com.geistindersh.aoc.helper

import kotlin.time.Duration
import kotlin.time.measureTime

/**
 * Standardized reporting function to avoid code duplication
 *
 * @param dayNumber The day number for the problem
 * @param part1 The solution to part 1 of the problem
 * @param part2 The solution to part 2 of the problem
 */
fun <T, V> report(
    year: Int,
    dayNumber: Int,
    part1: T,
    part2: V,
) {
    println("Year $year Day $dayNumber Part 1: $part1")
    println("Year $year Day $dayNumber Part 2: $part2")
}

/**
 * Common code to benchmark the runtime of a specific day of Advent of Code
 * without warming up the JIT compiler.
 *
 * @param init A function that initializes the DayN object, e.g. `{ Day1(DataFile.Part1) }`
 * @param part1Func A function that takes the DayN object, and returns the value for part1, e.g. `{ it.part1() }`
 * @param part2Func A function that takes the DayN object, and returns the value for part2, e.g. `{ it.part2() }`
 * @param verbose If the times and values for each of the parts should be printed to standard out
 *
 * @return The average duration of the [init], [part1Func], & [part2Func] across [count] number of runs
 */
@Suppress("unused")
fun <D, T, V> benchmarkColdStart(
    init: () -> D,
    part1Func: (D) -> T,
    part2Func: (D) -> V,
    verbose: Boolean = false,
) = benchmark(init, part1Func, part2Func, 0, 1, verbose)

/**
 * Common code to benchmark the runtime of a specific day of Advent of Code
 *
 * @param init A function that initializes the DayN object, e.g. `{ Day1(DataFile.Part1) }`
 * @param part1Func A function that takes the DayN object, and returns the value for part1, e.g. `{ it.part1() }`
 * @param part2Func A function that takes the DayN object, and returns the value for part2, e.g. `{ it.part2() }`
 * @param warmup The number of runs to do before measuring the time. Set to 0 to avoid warming up the JIT
 * @param count The number of runs to preform
 * @param verbose If the times and values for each of the parts should be printed to standard out
 *
 * @return The average duration of the [init], [part1Func], & [part2Func] across [count] number of runs
 */
@Suppress("unused")
fun <D, T, V> benchmark(
    init: () -> D,
    part1Func: (D) -> T,
    part2Func: (D) -> V,
    warmup: Int = 10,
    count: Int = 10,
    verbose: Boolean = false,
): Duration {
    for (i in 0..<warmup) {
        val day = init()
        part1Func(day)
        part2Func(day)
    }

    var totalTime = Duration.ZERO
    for (i in 0..<count) {
        var day: D
        val initTime = measureTime { day = init() }

        var p1: T
        val p1Time = measureTime { p1 = part1Func(day) }

        var p2: V
        val p2Time = measureTime { p2 = part2Func(day) }

        totalTime += initTime + p1Time + p2Time

        if (verbose) {
            println("Init Time: $initTime\tPart 1: $p1 ($p1Time)\tp2: $p2 ($p2Time)")
        }
    }
    return totalTime / count
}
