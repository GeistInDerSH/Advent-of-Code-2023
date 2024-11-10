package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day25Test {

    @Test
    fun part1() {
        assertEquals(14897079, Day25(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals("Pay the Deposit!", Day25(DataFile.Example).part2())
    }
}
