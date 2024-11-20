package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day4Test {
    @Test
    fun part1() {
        assertEquals(4512, Day4(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(1924, Day4(DataFile.Example).part2())
    }
}
