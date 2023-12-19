package day9

import helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day9KtTest {
    private val exampleInput = parseInput(DataFile.Example)
    private val input = parseInput(DataFile.Part1)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 114)
        assertEquals(part1(input), 1731106378)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 2)
        assertEquals(part2(input), 1087)
    }
}