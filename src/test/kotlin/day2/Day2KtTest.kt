package day2

import helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day2KtTest {
    private val partInput = parseInput(DataFile.Part1)
    private val exampleInput = parseInput(DataFile.Example)
    private val cubeCounts = Pull(12, 14, 13)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput, cubeCounts), 8)
        assertEquals(part1(partInput, cubeCounts), 2439)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 2286)
        assertEquals(part2(partInput), 63711)
    }
}