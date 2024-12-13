package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {
    @Test
    fun part1() {
        assertEquals(5, Day5(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(12, Day5(DataFile.Example).part2())
    }
}
