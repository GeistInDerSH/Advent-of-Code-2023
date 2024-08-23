package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day3Test {

    @Test
    fun part1() {
        assertEquals(157, Day3(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(70, Day3(DataFile.Example).part2())
    }
}