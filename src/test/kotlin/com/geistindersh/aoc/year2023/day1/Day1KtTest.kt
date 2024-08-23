package com.geistindersh.aoc.year2023.day1

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day1KtTest {
    private val numericOnly = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val nameAndNumeric = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "1", "2", "3", "4", "5", "6", "7", "8", "9"
    )

    @Test
    fun part1() {
        assertEquals(solve(DataFile.Example, numericOnly), 142)
        assertEquals(solve(DataFile.Part1, numericOnly), 56042)
    }

    @Test
    fun wordStringToNumeric() {
        val mapping = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
            "9" to 9,
        )

        mapping.forEach { (input, expected) ->
            assertEquals(input.wordStringToInt(), expected)
        }
    }

    @Test
    fun part2() {
        assertEquals(solve(DataFile.Example2, nameAndNumeric), 281)
        assertEquals(solve(DataFile.Part1, nameAndNumeric), 55358)
    }
}