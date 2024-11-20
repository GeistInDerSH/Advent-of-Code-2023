package com.geistindersh.aoc.year2023.day19

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GearProcessingTest {
    private val exampleInput = GearProcessing.parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 19114)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 167409079868000)
    }
}
