package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    @Test
    fun part1() {
        assertEquals("abcdffaa", Day11(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals("abcdffbb", Day11(DataFile.Example).part2())
    }
}
