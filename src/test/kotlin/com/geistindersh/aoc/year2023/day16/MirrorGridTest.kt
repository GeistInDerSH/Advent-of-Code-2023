package com.geistindersh.aoc.year2023.day16

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class MirrorGridTest {
    private val exampleInput = MirrorGrid.parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 46)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 51)
    }
}
