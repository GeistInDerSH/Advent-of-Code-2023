package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day3Test {
    @Test
    fun part1() {
        assertEquals(159, Day3(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(610, Day3(DataFile.Example).part2())
    }
}
