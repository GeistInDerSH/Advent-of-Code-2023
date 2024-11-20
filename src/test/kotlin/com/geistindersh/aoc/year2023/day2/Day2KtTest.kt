package com.geistindersh.aoc.year2023.day2

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day2KtTest {
    private val exampleInput = parseInput(DataFile.Example)
    private val cubeCounts = Pull(12, 14, 13)

    @Test
    fun part1() {
        assertEquals(part1(exampleInput, cubeCounts), 8)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleInput), 2286)
    }
}
