package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day8Test {
    @Test
    fun part1() {
        assertEquals(12, Day8(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(19, Day8(DataFile.Example).part2())
    }
}
