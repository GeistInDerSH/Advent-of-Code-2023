package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test
    fun part1() {
        assertEquals(25, Day12(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(286, Day12(DataFile.Example).part2())
    }
}
