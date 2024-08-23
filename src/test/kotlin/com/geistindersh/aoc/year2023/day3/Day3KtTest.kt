package com.geistindersh.aoc.year2023.day3

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day3KtTest {
    private val exampleData = parseInput(DataFile.Example)
    private val inputData = parseInput(DataFile.Part1)

    @Test
    fun part1() {
        assertEquals(part1(exampleData.first, exampleData.second), 4361)
        assertEquals(part1(inputData.first, inputData.second), 553079)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleData.first, exampleData.second), 467835)
        assertEquals(part2(inputData.first, inputData.second), 84363105)
    }
}