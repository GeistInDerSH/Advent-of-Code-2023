package day3

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day3KtTest {
    private val exampleData = parseInput("src/main/resources/day_3/example.txt")
    private val inputData = parseInput("src/main/resources/day_3/part_1.txt")

    @Test
    fun part1() {
        assertEquals(part1(exampleData.first, exampleData.second), 4361)
        assertEquals(part1(inputData.first, inputData.second), 553079)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleData.first, exampleData.second), 467835)
        assertEquals(part2(inputData.first, inputData.second), 84363105)
    }
}