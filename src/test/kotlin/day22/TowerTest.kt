package day22

import helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TowerTest {
    private val input = Tower.parseInput(DataFile.Part1)
    private val exampleInput = Tower.parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 5)
        assertEquals(input.part1(), 409)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 7)
        assertEquals(input.part2(), 61097)
    }
}