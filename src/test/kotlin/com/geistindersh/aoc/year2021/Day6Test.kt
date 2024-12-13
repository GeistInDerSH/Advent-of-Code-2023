package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day6Test {
    @Test
    fun part1() {
        assertEquals(5934L, Day6(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(26984457539L, Day6(DataFile.Example).part2())
    }
}
