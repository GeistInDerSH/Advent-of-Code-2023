package day11

import helper.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11KtTest {
    private val input = parseInput(DataFile.Part1)
    private val exampleInput = parseInput(DataFile.Example)

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