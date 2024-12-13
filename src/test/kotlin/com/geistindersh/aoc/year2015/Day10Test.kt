package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    @Test
    fun part1() {
        assertEquals(82350, Day10(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(1166642, Day10(DataFile.Example).part2())
    }
}
