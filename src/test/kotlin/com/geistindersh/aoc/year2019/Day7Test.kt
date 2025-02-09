package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day7Test {
    @Test
    fun part1() {
        assertEquals(65210, Day7(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(139629729, Day7(DataFile.Example2).part2())
    }
}
