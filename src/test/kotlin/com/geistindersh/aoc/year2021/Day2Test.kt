package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day2Test {
    @Test
    fun part1() {
        assertEquals(150, Day2(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(900, Day2(DataFile.Example).part2())
    }
}
