package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day8Test {
    @Test
    fun part1() {
        assertEquals(14, Day8(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(34, Day8(DataFile.Example).part2())
        assertEquals(-1, Day8(DataFile.Part1).part2())
    }
}
