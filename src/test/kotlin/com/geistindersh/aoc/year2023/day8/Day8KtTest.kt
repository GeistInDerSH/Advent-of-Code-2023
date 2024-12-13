package com.geistindersh.aoc.year2023.day8

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8KtTest {
    private val exampleInput = parseInput(DataFile.Example.filePath(2023, 8))

    @Test
    fun part1() {
        assertEquals(part1(exampleInput), 2)
    }
}
