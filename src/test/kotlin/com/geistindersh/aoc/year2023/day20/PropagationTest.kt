package com.geistindersh.aoc.year2023.day20

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PropagationTest {
    private val input = Propagation.parseInput(DataFile.Part1)
    private val exampleInput = Propagation.parseInput(DataFile.Example)
    private val exampleInput2 = Propagation.parseInput(DataFile.Example2)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 32000000)
        assertEquals(exampleInput2.part1(), 11687500)
        assertEquals(input.part1(), 886347020)
    }

    @Test
    fun part2() {
        assertEquals(input.part2(), 233283622908263)
    }
}