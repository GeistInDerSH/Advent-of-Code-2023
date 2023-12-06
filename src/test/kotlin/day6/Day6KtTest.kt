package day6

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day6KtTest {
    private val exampleInput = parseInput("src/main/resources/day_6/example.txt")
    private val actual = parseInput("src/main/resources/day_6/part_1.txt")

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 288)
        assertEquals(part1(actual), 741000)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 71503)
        assertEquals(part2(actual), 38220708)
    }
}