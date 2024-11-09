package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day19Test {

    @Test
    fun part1() {
        assertEquals(2, Day19(DataFile.Example).part1())
        assertEquals(250, Day19(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day19(DataFile.Example).part2())
        assertEquals(-1, Day19(DataFile.Part1).part2())
    }
}
