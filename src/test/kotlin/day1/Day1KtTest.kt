package day1

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
        assertEquals(solve("src/main/resources/day_1/example.txt", numericOnly), 142)
        assertEquals(solve("src/main/resources/day_1/part_1.txt", numericOnly), 56042)
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
            assertEquals(wordStringToInt(input), expected)
        }
    }

    @Test
    fun part2() {
        assertEquals(solve("src/main/resources/day_1/example_2.txt", nameAndNumeric), 281)
        assertEquals(solve("src/main/resources/day_1/part_1.txt", nameAndNumeric), 55358)
    }
}