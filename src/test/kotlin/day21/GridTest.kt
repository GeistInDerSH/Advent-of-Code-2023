package day21

import helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GridTest {
    private val input = Grid.parseInput(DataFile.Part1)
    private val exampleInput = Grid.parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(6), 16)
        assertEquals(input.part1(64), 3542)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(6), 16)
        assertEquals(exampleInput.part2(10), 50)
        // assertEquals(input.part2(26501365), 593174122420825)
    }
}