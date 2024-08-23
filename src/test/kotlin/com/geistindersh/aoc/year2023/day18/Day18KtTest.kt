package com.geistindersh.aoc.year2023.day18

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day18KtTest {
    private val example = DataFile.Example

    @Test
    fun part1() {
        assertEquals(part1(example), 62)
    }

    @Test
    fun part2() {
        assertEquals(part2(example), 952408144115)
    }
}