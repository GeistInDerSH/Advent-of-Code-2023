package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
    @Test
    fun part1() {
        assertEquals(10092, Day15(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(9021, Day15(DataFile.Example).part2())
    }
}
