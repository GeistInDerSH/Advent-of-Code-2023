package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Test {
    @Test
    fun part1() {
        assertEquals(6, Day19(DataFile.Example).part1())
        assertEquals(-1, Day19(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day19(DataFile.Example).part2())
        assertEquals(-1, Day19(DataFile.Part1).part2())
    }
}
