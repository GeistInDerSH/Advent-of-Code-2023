package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6Test {
    @Test
    fun part1() {
        assertEquals(5, Day6(DataFile.Example).part1())
        assertEquals(6, Day6(DataFile.Example2).part1())
    }

    @Test
    fun part2() {
        assertEquals(23, Day6(DataFile.Example).part2())
        assertEquals(23, Day6(DataFile.Example2).part2())
    }
}
