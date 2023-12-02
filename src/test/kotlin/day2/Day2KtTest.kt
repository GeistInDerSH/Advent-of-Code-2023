package day2

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day2KtTest {
    private val partInput = parseInput("src/main/resources/day_2/part_1.txt")
    private val exampleInput = parseInput("src/main/resources/day_2/example.txt")
    private val cubeCounts = Pull(12, 14, 13)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput, cubeCounts), 8)
        assertEquals(part1(partInput, cubeCounts), 2439)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 2286)
        assertEquals(part2(partInput), 63711)
    }
}