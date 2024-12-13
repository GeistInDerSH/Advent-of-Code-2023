package com.geistindersh.aoc.year2023.day23

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class HikeTest {
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 94)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 154)
    }
}
