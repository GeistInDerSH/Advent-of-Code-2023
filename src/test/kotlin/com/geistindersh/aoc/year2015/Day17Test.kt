package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    @Test
    fun part1() {
        assertEquals(4, Day17(DataFile.Example).part1(25))
    }

    @Test
    fun part2() {
        assertEquals(3, Day17(DataFile.Example).part2(25))
    }
}
