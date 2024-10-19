package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22Test {

    @Test
    fun part1() {
        assertEquals(6032, Day22(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(4578, Day22(DataFile.Part1).part2())
    }
}