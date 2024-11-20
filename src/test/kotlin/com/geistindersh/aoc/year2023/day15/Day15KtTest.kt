package com.geistindersh.aoc.year2023.day15

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day15KtTest {
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 1320)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 145)
    }
}
