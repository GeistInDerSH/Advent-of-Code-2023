package com.geistindersh.aoc.year2023.day17

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17KtTest {

    @Test
    fun part1() {
        assertEquals(part1(DataFile.Example), 102)
        assertEquals(part1(DataFile.Part1), 859)
    }

    @Test
    fun part2() {
        assertEquals(part2(DataFile.Example), 94)
        assertEquals(part2(DataFile.Part1), 1027)
    }
}