package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {
    @Test
    fun part1() {
        // There isn't an output instruction
        assertEquals(-1, Day5(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day5(DataFile.Example).part2())
        assertEquals(-1, Day5(DataFile.Part1).part2())
    }
}
