package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    @Test
    fun part1() {
        assertEquals(67384529, Day23(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(149245887792, Day23(DataFile.Example).part2())
    }
}
