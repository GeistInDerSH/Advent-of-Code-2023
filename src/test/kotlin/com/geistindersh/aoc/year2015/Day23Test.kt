package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    @Test
    fun part1() {
        assertEquals(2, Day23(DataFile.Example, "a").part1())
    }

    @Test
    fun part2() {
        assertEquals(7, Day23(DataFile.Example, "a").part2())
    }
}
