package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day9Test {

    @Test
    fun part1() {
        assertEquals(127, Day9(DataFile.Example, 5).part1())
    }

    @Test
    fun part2() {
        assertEquals(62, Day9(DataFile.Example, 5).part2())
    }
}
