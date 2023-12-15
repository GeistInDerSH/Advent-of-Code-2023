package day15

import helper.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day15KtTest {
    private val input = parseInput(DataFile.Part1)
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 1320)
        assertEquals(part1(input), 501680)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 145)
        assertEquals(part2(input), 241094)
    }
}