package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day3Test {
    @Test
    fun part1() {
        assertEquals(7, Day3(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(336, Day3(DataFile.Example).part2())
    }
}
