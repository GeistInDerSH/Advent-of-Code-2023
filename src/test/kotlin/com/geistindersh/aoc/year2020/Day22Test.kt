package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
    @Test
    fun part1() {
        assertEquals(306, Day22(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(291, Day22(DataFile.Example).part2())
    }
}
