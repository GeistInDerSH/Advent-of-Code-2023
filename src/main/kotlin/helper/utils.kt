package helper

import java.io.File

fun fileToStream(fileName: String): Sequence<String> = File(fileName).inputStream().bufferedReader().lineSequence()

fun <T, V> report(dayNumber: Int, part1: T, part2: V) {
    println("Day $dayNumber Part 1: $part1")
    println("Day $dayNumber Part 2: $part2")
}