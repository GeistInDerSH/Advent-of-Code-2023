package com.geistindersh.aoc.year2023.day22

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TowerTest {
    private val exampleInput = Tower.parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 5)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 7)
    }
}
