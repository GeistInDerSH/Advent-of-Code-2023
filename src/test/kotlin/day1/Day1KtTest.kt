package day1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day1KtTest {

    @Test
    fun part1() {
        assertEquals(part1("src/main/resources/day_1/example.txt"), 142)
        assertEquals(part1("src/main/resources/day_1/part_1.txt"), 56042)
    }

    @Test
    fun wordStringToNumeric() {
        val mapping = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9",
            "9" to "9",
        )

        mapping.forEach { (input, expected) ->
            assertEquals(wordStringToNumeric(input), expected)
        }
    }

    @Test
    fun part2() {
        assertEquals(part2("src/main/resources/day_1/example_2.txt"), 281)
        assertEquals(part2("src/main/resources/day_1/part_1.txt"), 55358)
    }
}