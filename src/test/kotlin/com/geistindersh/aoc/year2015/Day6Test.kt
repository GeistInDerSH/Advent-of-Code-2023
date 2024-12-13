package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day6Test {
    @Test
    fun part1() {
        assertEquals(1_000_000 - 1000 - 4, Day6(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(1001996, Day6(DataFile.Example).part2())
    }
}
