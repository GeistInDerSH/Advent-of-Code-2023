package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    @Test
    fun part1() {
        assertEquals(24, Day14(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(93, Day14(DataFile.Example).part2())
    }
}
