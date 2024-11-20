package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day24Test {
    @Test
    fun part1() {
        assertEquals(18, Day24(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(54, Day24(DataFile.Example).part2())
    }
}
