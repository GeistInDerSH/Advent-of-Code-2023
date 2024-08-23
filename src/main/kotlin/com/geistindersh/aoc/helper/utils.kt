package com.geistindersh.aoc.helper

/**
 * Standardized reporting function to avoid code duplication
 *
 * @param dayNumber The day number for the problem
 * @param part1 The solution to part 1 of the problem
 * @param part2 The solution to part 2 of the problem
 */
fun <T, V> report(year: Int, dayNumber: Int, part1: T, part2: V) {
    println("Year $year Day $dayNumber Part 1: $part1")
    println("Year $year Day $dayNumber Part 2: $part2")
}