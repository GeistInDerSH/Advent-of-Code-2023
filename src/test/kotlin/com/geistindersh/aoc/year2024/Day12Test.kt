package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {
    @Test
    fun part1() {
        assertEquals(1930, Day12(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(1206, Day12(DataFile.Example).part2())
    }
}
