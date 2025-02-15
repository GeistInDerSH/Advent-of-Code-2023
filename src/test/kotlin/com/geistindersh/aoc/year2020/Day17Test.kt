package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    @Test
    fun part1() {
        assertEquals(112, Day17(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(848, Day17(DataFile.Example).part2())
    }
}
