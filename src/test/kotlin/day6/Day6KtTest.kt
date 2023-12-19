package day6

import helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day6KtTest {
    private val exampleInput = parseInput(DataFile.Example)
    private val actual = parseInput(DataFile.Part1)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 288)
        assertEquals(part1(actual), 741000)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 71503)
        assertEquals(part2(actual), 38220708)
    }
}