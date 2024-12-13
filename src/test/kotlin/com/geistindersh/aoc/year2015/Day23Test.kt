package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

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
