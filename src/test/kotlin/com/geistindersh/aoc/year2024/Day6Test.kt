package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6Test {
    @Test
    fun part1() {
        assertEquals(41, Day6(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(6, Day6(DataFile.Example).part2())
    }
}
