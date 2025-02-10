package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day9Test {
    @Test
    fun part1() {
        assertEquals(1125899906842624, Day9(DataFile.Example).part1())
        assertEquals(-1, Day9(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day9(DataFile.Example).part2())
        assertEquals(-1, Day9(DataFile.Part1).part2())
    }
}
