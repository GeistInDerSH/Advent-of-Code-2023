package com.geistindersh.aoc.year2023.day14

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GridTest {
    private val exampleInput = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 136)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 64)
    }
}
