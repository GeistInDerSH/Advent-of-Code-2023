package day4

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day4KtTest {
    private val exampleInput = parseInput("src/main/resources/day_4/example.txt")
    private val actual = parseInput("src/main/resources/day_4/part_1.txt")

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 13)
        assertEquals(part1(actual), 22193)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 30)
        assertEquals(part2(actual), 5625994)
    }
}