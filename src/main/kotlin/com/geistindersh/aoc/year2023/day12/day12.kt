package com.geistindersh.aoc.year2023.day12

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

data class SpringRecord(val springs: String, val records: List<Int>) {
    // Cache to avoid repeated re-calculations of known values
    private val memorization = mutableMapOf<Pair<String, List<Int>>, Long>()

    /**
     * @return The number of permutations for the [springs] and [records]
     */
    fun permutationCount() = permutationCount(springs, records)

    /**
     * @param input The input string to check against
     * @param records The records of known broken spring count
     * @return The number of permutations for the [input] and [records]
     */
    private fun permutationCount(input: String, records: List<Int>): Long {
        // Check the cache
        val pairedInput = input to records
        if (pairedInput in memorization) {
            return memorization[pairedInput]!!
        } else if (input.isBlank()) {
            return if (records.isEmpty()) 1 else 0
        }

        val permutations = when (input.first()) {
            // . means we can just move on to the next
            '.' -> permutationCount(input.substring(1), records)
            // ? means we have to check if it is . or #
            '?' -> {
                val remaining = input.substring(1)
                permutationCount(".$remaining", records) +
                        permutationCount("#$remaining", records)
            }

            // # means that the spring is damaged
            '#' -> {
                if (records.isEmpty()) {
                    0
                } else {
                    val damaged = records.first()
                    val areAllValid = input.take(damaged).all { it == '#' || it == '?' }

                    if (damaged <= input.length && areAllValid) {
                        // Drop the first value
                        val updatedRecords = records.subList(1, records.size)
                        when {
                            // If the number of damaged springs is the same as the length,
                            // it matters if we have more records after
                            damaged == input.length -> if (updatedRecords.isEmpty()) 1 else 0
                            // skip ahead and check if it is . or ? then recalculate with the updated records
                            input[damaged] == '.' -> permutationCount(input.substring(damaged + 1), updatedRecords)
                            input[damaged] == '?' -> permutationCount(input.substring(damaged + 1), updatedRecords)
                            else -> 0
                        }
                    } else {
                        0
                    }
                }
            }

            else -> 0
        }

        // Save out the result to avoid re-calculating
        memorization[pairedInput] = permutations

        return permutations
    }
}

fun parseInput(fileType: DataFile, unfoldCount: Int): List<SpringRecord> {
    return fileToStream(2023, 12, fileType)
        .map { line ->
            val (rawSprings, data) = line.split(' ')
            val rawRecords = data.split(',').map { it.toInt() }

            val springs = if (unfoldCount > 0) {
                // ##. -> ##.?##.?##.?
                (0..<unfoldCount - 1).joinToString(separator = "") { "$rawSprings?" } + rawSprings
            } else {
                // ##. -> ##.
                rawSprings
            }

            val records = if (unfoldCount > 0) {
                (0..<unfoldCount).flatMap { rawRecords }
            } else {
                rawRecords
            }

            SpringRecord(springs, records)
        }
        .toList()
}

fun solution(input: List<SpringRecord>): Long {
    return input
        .parallelStream()
        .map { it.permutationCount() }
        .reduce(0) { acc, p -> acc + p }
}

fun part1(fileType: DataFile): Long = solution(parseInput(fileType, 0))
fun part2(fileType: DataFile): Long = solution(parseInput(fileType, 5))

fun day12() {
    val fileName = DataFile.Part1

    report(
        year = 2023,
        dayNumber = 12,
        part1 = part1(fileName),
        part2 = part2(fileName),
    )
}