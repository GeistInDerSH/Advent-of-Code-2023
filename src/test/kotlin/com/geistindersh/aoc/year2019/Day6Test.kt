package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day6Test {
    @Test
    fun part1() {
        assertEquals(42, Day6(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(4, Day6(DataFile.Example2).part2())
    }
}
