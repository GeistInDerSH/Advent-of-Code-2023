package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun part1() {
        assertEquals(220, Day10(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(19208, Day10(DataFile.Example).part2())
    }
}
