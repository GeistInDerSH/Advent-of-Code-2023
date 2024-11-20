package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Day22Test {
    @Test
    fun part1() {
        assertEquals(641, Day22(DataFile.Example).part1(10, 250))
    }

    @Test
    fun part2() {
        assertThrows<Exception> { Day22(DataFile.Example).part2(10, 250) }
    }
}
