package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day24Test {
    @Test
    fun part1() {
        assertEquals(10, Day24(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(2208, Day24(DataFile.Example).part2())
    }
}
