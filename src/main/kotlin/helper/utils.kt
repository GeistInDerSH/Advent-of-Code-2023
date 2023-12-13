package helper

import java.io.File

/**
 * This is a utility for making reading large files easier when doing the actual solutions
 *
 * @param fileName The file to read
 * @return An iterable sequence of strings
 */
fun fileToStream(fileName: String): Sequence<String> = File(fileName).inputStream().bufferedReader().lineSequence()

/**
 * Standardized reporting function to avoid code duplication
 *
 * @param dayNumber The day number for the problem
 * @param part1 The solution to part 1 of the problem
 * @param part2 The solution to part 2 of the problem
 */
fun <T, V> report(dayNumber: Int, part1: T, part2: V) {
    println("Day $dayNumber Part 1: $part1")
    println("Day $dayNumber Part 2: $part2")
}