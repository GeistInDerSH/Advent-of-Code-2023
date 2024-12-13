package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    @Test
    fun part1() {
        assertEquals(1120, Day14(DataFile.Example).part1(1000))
    }

    @Test
    fun part2() {
        assertEquals(689, Day14(DataFile.Example).part2(1000))
    }
}
