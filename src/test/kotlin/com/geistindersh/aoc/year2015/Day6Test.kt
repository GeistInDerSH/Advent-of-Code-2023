package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6Test {

    @Test
    fun part1() {
        assertEquals(1_000_000 - 1000 - 4, Day6(DataFile.Example).part1())
        assertEquals(-1, Day6(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day6(DataFile.Example).part2())
    }
}
