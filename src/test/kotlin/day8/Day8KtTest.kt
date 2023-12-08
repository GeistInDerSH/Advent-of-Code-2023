package day8

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day8KtTest {
    private val input = parseInput("src/main/resources/day_8/part_1.txt")
    private val exampleInput = parseInput("src/main/resources/day_8/example.txt")
    private val exampleInput2 = parseInput("src/main/resources/day_8/example_2.txt")
    private val exampleInputPart2 = parseInput("src/main/resources/day_8/example_part_2.txt")

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 2)
        assertEquals(part1(exampleInput2), 6)
        assertEquals(part1(input), 13301)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInputPart2), 6)
        assertEquals(part2(input), 7309459565207)
    }
}