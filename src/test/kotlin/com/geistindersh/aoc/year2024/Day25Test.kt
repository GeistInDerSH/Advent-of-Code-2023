package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day25Test {
    @Test
    fun part1() {
        assertEquals(3, Day25(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day25(DataFile.Example).part2())
        assertEquals(-1, Day25(DataFile.Part1).part2())
    }
}
