package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
    @Test
    fun part1() {
        assertEquals(641, Day22(DataFile.Example, 10, 250).part1())
    }

    @Test
    fun part2() {
        assertThrows<Exception> { Day22(DataFile.Example, 10, 250).part2() }
    }
}
