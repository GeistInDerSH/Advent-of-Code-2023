package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day7Test {
    @Test
    fun part1() {
        assertEquals(65079u, Day7(DataFile.Example).part1("i").toUInt())
    }

    @Test
    fun part2() {
        assertEquals(65079u, Day7(DataFile.Example).part2("i").toUInt())
    }
}
