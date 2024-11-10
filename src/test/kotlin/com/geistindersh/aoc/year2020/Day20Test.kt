package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day20Test {

    @Test
    fun part1() {
        assertEquals(20899048083289, Day20(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day20(DataFile.Example).part2())
        assertEquals(-1, Day20(DataFile.Part1).part2())
    }
}
