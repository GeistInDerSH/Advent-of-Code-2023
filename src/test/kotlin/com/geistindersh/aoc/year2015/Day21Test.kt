package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {
    @Test
    fun part1() {
        assertEquals(65, Day21(DataFile.Example, 8).part1())
    }

    @Test
    fun part2() {
        assertEquals(188, Day21(DataFile.Example, 8).part2())
    }
}
