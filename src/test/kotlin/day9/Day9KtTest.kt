package day9

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day9KtTest {
    private val exampleInput = parseInput("src/main/resources/day_9/example.txt")
    private val input = parseInput("src/main/resources/day_9/part_1.txt")

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 114)
        assertEquals(part1(input), 1731106378)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 2)
        assertEquals(part2(input), 1087)
    }
}