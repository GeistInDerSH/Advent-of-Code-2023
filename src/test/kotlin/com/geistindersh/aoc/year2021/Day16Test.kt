package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {
    @Test
    fun part1() {
        assertEquals(16, Day16(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(15, Day16(DataFile.Example).part2())
    }
}
