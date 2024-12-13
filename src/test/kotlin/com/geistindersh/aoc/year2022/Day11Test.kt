package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    @Test
    fun part1() {
        assertEquals(BigInteger.valueOf(10605), Day11(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(BigInteger.valueOf(2713310158), Day11(DataFile.Example).part2())
    }
}
