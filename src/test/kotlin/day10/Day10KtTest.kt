package day10

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10KtTest {
    private val input = parseInput("src/main/resources/day_10/part_1.txt")
    private val exampleInput = parseInput("src/main/resources/day_10/example.txt")

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 4)
        assertEquals(part1(input), 6800)
    }

    @Test
    fun part2() {
        assertEquals(part2(input), 483)
    }
}