package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day3Test {
    @Test
    fun part1() {
        assertEquals(157, Day3(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(70, Day3(DataFile.Example).part2())
    }
}
