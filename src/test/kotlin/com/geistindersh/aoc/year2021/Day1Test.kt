package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day1Test {
    @Test
    fun part1() {
        assertEquals(7, Day1(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(5, Day1(DataFile.Example).part2())
    }
}
