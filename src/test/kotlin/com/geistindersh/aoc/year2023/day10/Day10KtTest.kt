package com.geistindersh.aoc.year2023.day10

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10KtTest {
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 4)
    }
}
