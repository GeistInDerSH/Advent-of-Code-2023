package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day1Test {
    private val day1 = Day1(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(24000, day1.part1())
    }

    @Test
    fun part2() {
        assertEquals(45000, day1.part2())
    }
}