package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day9Test {
    @Test
    fun part1() {
        assertEquals(1928, Day9(DataFile.Example).part1())
        assertEquals(-1, Day9(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day9(DataFile.Example).part2())
        assertEquals(-1, Day9(DataFile.Part1).part2())
    }
}
