package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test
    fun part1() {
        assertEquals(179, Day12(DataFile.Example, 10).part1())
    }

    @Test
    fun part2() {
        assertEquals(2772, Day12(DataFile.Example).part2())
        assertEquals(4686774924, Day12(DataFile.Example2).part2())
    }
}
