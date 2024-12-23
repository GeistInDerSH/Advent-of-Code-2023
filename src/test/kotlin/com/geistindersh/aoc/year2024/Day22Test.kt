package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
    @Test
    fun part1() {
        assertEquals(37327623, Day22(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(23, Day22(DataFile.Example2).part2())
    }
}
