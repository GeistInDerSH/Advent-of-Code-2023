package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8Test {
    @Test
    fun part1() {
        assertEquals(1, Day8(DataFile.Example, 3, 2).part1())
    }

    @Test
    fun part2() {
        assertEquals(" #\n# \n", Day8(DataFile.Example2, 2, 2).part2())
    }
}
