package day10

import helper.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10KtTest {
    private val input = parseInput(DataFile.Part1)
    private val exampleInput = parseInput(DataFile.Example)

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