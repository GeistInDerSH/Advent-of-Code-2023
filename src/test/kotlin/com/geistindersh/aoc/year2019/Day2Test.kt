package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day2Test {
    @Test
    fun part1() {
        assertEquals(3500, Day2(DataFile.Example).part1(emptyMap()))
    }

    @Test
    fun part2() {
        assertEquals(-1, Day2(DataFile.Example).part2())
        assertEquals(-1, Day2(DataFile.Part1).part2())
    }
}
