package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day25Test {
    @Test
    fun part1() {
        assertEquals(7726640, Day25(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals("Push the button!", Day25(DataFile.Example).part2())
    }
}
