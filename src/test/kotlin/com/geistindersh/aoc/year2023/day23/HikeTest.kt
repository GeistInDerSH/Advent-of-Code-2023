package com.geistindersh.aoc.year2023.day23

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HikeTest {
    private val input = parseInput(DataFile.Part1)
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 94)
        assertEquals(input.part1(), 2186)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 154)
        assertEquals(input.part2(), 6802)
    }
}