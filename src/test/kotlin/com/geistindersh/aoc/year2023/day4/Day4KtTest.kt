package com.geistindersh.aoc.year2023.day4

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day4KtTest {
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 13)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 30)
    }
}
