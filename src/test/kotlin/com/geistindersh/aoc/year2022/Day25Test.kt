package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day25Test {
    @Test
    fun part1() {
        assertEquals("2=-1=0", Day25(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals("Push the button!", Day25(DataFile.Example).part2())
    }
}
