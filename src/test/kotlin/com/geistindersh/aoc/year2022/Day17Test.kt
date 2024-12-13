package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    @Test
    fun part1() {
        assertEquals(3068, Day17(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(1514285714288, Day17(DataFile.Example).part2())
    }
}
