package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test
    fun part1() {
        assertEquals(64, Day18(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(58, Day18(DataFile.Example).part2())
    }
}
