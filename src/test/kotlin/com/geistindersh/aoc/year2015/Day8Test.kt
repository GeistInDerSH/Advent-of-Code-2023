package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8Test {
    @Test
    fun part1() {
        assertEquals(12, Day8(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(19, Day8(DataFile.Example).part2())
    }
}
