package com.geistindersh.aoc.year2023.day7

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day7KtTest {
    @Test
    fun part1() {
        assertEquals(part1(DataFile.Example), 6440)
    }

    @Test
    fun part2() {
        assertEquals(part2(DataFile.Example), 5905)
    }
}
