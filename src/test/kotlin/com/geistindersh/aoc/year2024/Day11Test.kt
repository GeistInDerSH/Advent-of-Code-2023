package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun part1() {
        assertEquals(55312, Day11(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(65601038650482, Day11(DataFile.Example).part2())
    }
}
