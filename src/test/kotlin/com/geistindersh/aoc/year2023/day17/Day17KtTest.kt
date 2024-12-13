package com.geistindersh.aoc.year2023.day17

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17KtTest {
    @Test
    fun part1() {
        assertEquals(part1(DataFile.Example), 102)
    }

    @Test
    fun part2() {
        assertEquals(part2(DataFile.Example), 94)
    }
}
