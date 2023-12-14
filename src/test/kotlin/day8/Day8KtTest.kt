package day8

import helper.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day8KtTest {
    private val input = parseInput(DataFile.Part1.filePath(8))
    private val exampleInput = parseInput(DataFile.Example.filePath(8))
    private val exampleInput2 = parseInput(DataFile.Example2.filePath(8))
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