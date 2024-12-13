package com.geistindersh.aoc.year2023.day24

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class HailstonesTest {
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(7, 27), 2)
    }
}
