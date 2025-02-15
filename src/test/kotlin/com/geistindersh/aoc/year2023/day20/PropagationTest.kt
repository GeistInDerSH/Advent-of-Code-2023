package com.geistindersh.aoc.year2023.day20

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class PropagationTest {
    private val exampleInput = Propagation.parseInput(DataFile.Example)
    private val exampleInput2 = Propagation.parseInput(DataFile.Example2)

    @Test
    fun part1() {
        assertEquals(exampleInput.part1(), 32000000)
        assertEquals(exampleInput2.part1(), 11687500)
    }
}
