package com.geistindersh.aoc.year2023.day12

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12KtTest {
    private val exampleFileName = DataFile.Example

    @Test
    fun part1() {
        assertEquals(part1(exampleFileName), 21)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleFileName), 525152)
    }
}
