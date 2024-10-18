package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day20Test {

    @Test
    fun part1() {
        assertEquals(3, Day20(DataFile.Example).part1())
        assertEquals(4578, Day20(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(1623178306, Day20(DataFile.Example).part2())
        assertEquals(2159638736133, Day20(DataFile.Part1).part2())
    }
}