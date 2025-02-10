package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    @Test
    fun part1() {
        assertEquals(33, Day10(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day10(DataFile.Example).part2())
        assertEquals(-1, Day10(DataFile.Part1).part2())
    }
}
