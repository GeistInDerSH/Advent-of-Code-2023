package day13

import helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13KtTest {
    private val input = parseInput(DataFile.Part1)
    private val exampleInput = parseInput(DataFile.Example)

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