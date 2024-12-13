package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8Test {
    @Test
    fun part1() {
        assertEquals(26, Day8(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(61229, Day8(DataFile.Example).part2())
    }
}
