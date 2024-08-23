package com.geistindersh.aoc.year2023.day9

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day9KtTest {
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 114)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 2)
    }
}