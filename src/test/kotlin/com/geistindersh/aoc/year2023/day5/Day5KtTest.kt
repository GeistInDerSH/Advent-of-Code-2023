package com.geistindersh.aoc.year2023.day5

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day5KtTest {
    private val input = parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(part1(input), 35)
    }

    @Test
    fun part2() {
        assertEquals(part2(input), 46)
    }
}