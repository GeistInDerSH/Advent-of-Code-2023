package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test
    fun part1() {
        assertEquals(4, Day18(DataFile.Example, 4).part1())
    }

    @Test
    fun part2() {
        assertEquals(17, Day18(DataFile.Example, 5).part2())
    }
}
