package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
    @Test
    fun part1() {
        assertEquals(26, Day15(DataFile.Example, 10).part1())
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES)
    fun part2() {
        assertEquals(56000011, Day15(DataFile.Example, 10).part2())
    }
}
