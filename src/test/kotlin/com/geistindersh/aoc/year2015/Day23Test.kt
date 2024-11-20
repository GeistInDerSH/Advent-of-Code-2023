package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day23Test {
    @Test
    fun part1() {
        assertEquals(2, Day23(DataFile.Example).part1("a"))
    }

    @Test
    fun part2() {
        assertEquals(7, Day23(DataFile.Example).part2("a"))
    }
}
