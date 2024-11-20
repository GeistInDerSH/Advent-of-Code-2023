package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun part1() {
        assertEquals(37, Day11(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(26, Day11(DataFile.Example).part2())
    }
}
