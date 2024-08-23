package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day7Test {

    @Test
    fun part1() {
        assertEquals(95437, Day7(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(24933642, Day7(DataFile.Example).part2())
    }
}