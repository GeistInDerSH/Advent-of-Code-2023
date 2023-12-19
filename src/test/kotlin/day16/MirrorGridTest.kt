package day16

import helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MirrorGridTest {
    private val input = MirrorGrid.parseInput(DataFile.Part1)
    private val exampleInput = MirrorGrid.parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 46)
        assertEquals(input.part1(), 7927)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 51)
        assertEquals(input.part2(), 8246)
    }
}