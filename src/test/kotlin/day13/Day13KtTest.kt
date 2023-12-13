package day13

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13KtTest {
    private val input = parseInput("src/main/resources/day_13/part_1.txt")
    private val exampleInput = parseInput("src/main/resources/day_13/example.txt")

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 405)
        assertEquals(part1(input), 30158)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 400)
        assertEquals(part2(input), 36474)
    }
}