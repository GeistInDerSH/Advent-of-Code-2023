package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {

    @Test
    fun part1() {
        assertEquals(6, Day10(DataFile.Example).part1(5))
        assertEquals(0, Day10(DataFile.Part1).part1(40))
    }

    @Test
    fun part2() {
        assertEquals(-1, Day10(DataFile.Example).part2())
    }
}
