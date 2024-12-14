package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    @Test
    fun part1() {
        assertEquals(12, Day14(DataFile.Example).part1(7, 11))
    }

    @Test
    fun part2() {
        assertEquals(-1, Day14(DataFile.Example).part2())
        assertEquals(-1, Day14(DataFile.Part1).part2())
    }
}
