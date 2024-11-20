package com.geistindersh.aoc.year2023.day6

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day6KtTest {
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 288)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 71503)
    }
}
