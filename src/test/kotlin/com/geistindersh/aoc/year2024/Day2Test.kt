package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day2Test {
    @Test
    fun part1() {
        assertEquals(2, Day2(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(4, Day2(DataFile.Example).part2())
    }
}
