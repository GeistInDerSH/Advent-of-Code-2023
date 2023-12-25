package day24

import helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HailstonesTest {
    private val input = parseInput(DataFile.Part1)
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(7, 27), 2)
        assertEquals(input.part1(200000000000000, 400000000000000), 19523)
    }

    @Test
    fun part2() {
        assertEquals(input.part2(), 566373506408017)
    }
}