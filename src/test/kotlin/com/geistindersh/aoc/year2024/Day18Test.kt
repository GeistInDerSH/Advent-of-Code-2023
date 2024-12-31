package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test
    fun part1() {
        assertEquals(22, Day18(DataFile.Example, 6, 6, 12).part1())
    }

    @Test
    fun part2() {
        assertEquals(Point2D(6, 1), Day18(DataFile.Example, 6, 6).part2())
    }
}
