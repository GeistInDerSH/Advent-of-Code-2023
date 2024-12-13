package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day9Test {
    @Test
    fun part1() {
        assertEquals(15, Day9(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(1134, Day9(DataFile.Example).part2())
    }
}
