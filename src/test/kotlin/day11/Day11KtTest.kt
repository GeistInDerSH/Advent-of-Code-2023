package day11

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11KtTest {
    private val input = parseInput("src/main/resources/day_11/part_1.txt")
    private val exampleInput = parseInput("src/main/resources/day_11/example.txt")

    @Test
    fun part1() {
        assertEquals(part1(input), 9543156)
        assertEquals(part1(exampleInput), 374)
    }

    @Test
    fun part2() {
        assertEquals(part2(input), 625243292686)
    }
}