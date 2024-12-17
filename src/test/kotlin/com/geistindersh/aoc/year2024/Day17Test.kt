package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    @Test
    fun part1() {
        assertEquals("4,6,3,5,6,3,5,2,1,0", Day17(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day17(DataFile.Example).part2())
        assertEquals(-1, Day17(DataFile.Part1).part2())
    }
}
