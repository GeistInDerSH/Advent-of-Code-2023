package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test
    fun part1() {
        assertEquals(6, Day12(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(4, Day12(DataFile.Example).part2())
    }
}
