package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test
    fun part1() {
        assertEquals(31, Day12(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(29, Day12(DataFile.Example).part2())
    }
}
