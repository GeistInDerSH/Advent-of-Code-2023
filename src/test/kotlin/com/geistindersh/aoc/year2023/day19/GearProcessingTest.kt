package com.geistindersh.aoc.year2023.day19

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GearProcessingTest {
    private val input = GearProcessing.parseInput(DataFile.Part1)
    private val exampleInput = GearProcessing.parseInput(DataFile.Example)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 19114)
        assertEquals(input.part1(), 476889)
    }

    @Test
    fun part2() {
        assertEquals(exampleInput.part2(), 167409079868000)
        assertEquals(input.part2(), 132380153677887)
    }
}