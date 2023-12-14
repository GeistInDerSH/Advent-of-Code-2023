package day4

import helper.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day4KtTest {
    private val exampleInput = parseInput(DataFile.Example)
    private val actual = parseInput(DataFile.Part1)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 13)
        assertEquals(part1(actual), 22193)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 30)
        assertEquals(part2(actual), 5625994)
    }
}