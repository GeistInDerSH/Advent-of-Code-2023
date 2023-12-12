package day12

import helper.fileToStream
import helper.report

data class SpringRecord(val springs: String, val records: List<Int>) {
    private var permutations: Long? = null
    private val memorization = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun permutationCount(): Long {
        return if (permutations != null) {
            permutations!!
        } else {
            permutations = permutationCount(springs, records)
            permutations!!
        }
    }

    private fun permutationCount(input: String, records: List<Int>): Long {
        val pairedInput = input to records
        if (pairedInput in memorization) {
            return memorization[pairedInput]!!
        } else if (input.isBlank()) {
            return if (records.isEmpty()) 1 else 0
        }

        val char = input.first()
        val remaining = input.substring(1)
        val permutations = when (char) {
            '.' -> permutationCount(remaining, records)
            '?' -> permutationCount(".$remaining", records) + permutationCount("#$remaining", records)
            '#' -> {
                if (records.isEmpty()) {
                    0
                } else {
                    val damaged = records.first()
                    val areAllValid = input.chars().limit(damaged.toLong()).allMatch { it.toChar() in setOf('#', '?') }

                    if (damaged <= input.length && areAllValid) {
                        val updatedRecords = records.subList(1, records.size)
                        if (damaged == input.length) {
                            if (updatedRecords.isEmpty()) 1 else 0
                        } else if (input[damaged] == '.') {
                            permutationCount(input.substring(damaged + 1), updatedRecords)
                        } else if (input[damaged] == '?') {
                            permutationCount(".${input.substring(damaged + 1)}", updatedRecords)
                        } else {
                            0
                        }
                    } else {
                        0
                    }
                }
            }

            else -> {
                0
            }
        }

        memorization[pairedInput] = permutations

        return permutations
    }
}

fun parseInput(fileName: String, unfoldCount: Int): List<SpringRecord> {
    return fileToStream(fileName).map { line ->
        val (rawSprings, data) = line.split(' ')
        val rawRecords = data.split(',').map { it.toInt() }

        val springs = if (unfoldCount > 0) {
            (0..<unfoldCount - 1).joinToString(separator = "") { "$rawSprings?" } + rawSprings
        } else {
            rawSprings
        }

        val records = if (unfoldCount > 0) {
            (0..<unfoldCount).flatMap { rawRecords }
        } else {
            rawRecords
        }

        SpringRecord(springs, records)
    }.toList()
}

fun solution(input: List<SpringRecord>): Long = input.sumOf { it.permutationCount() }

fun part1(fileName: String): Long = solution(parseInput(fileName, 0))
fun part2(fileName: String): Long = solution(parseInput(fileName, 5))

fun day12() {
    val fileName = "src/main/resources/day_12/part_1.txt"

    report(
        dayNumber = 12,
        part1 = part1(fileName),
        part2 = part2(fileName),
    )
}