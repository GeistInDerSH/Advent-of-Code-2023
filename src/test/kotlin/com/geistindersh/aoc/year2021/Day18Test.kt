package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day18Test {

	@Test
	fun part1() {
		assertEquals(3488, Day18(DataFile.Example).part1())
		assertEquals(-1, Day18(DataFile.Part1).part1())
	}

	@Test
	fun part2() {
		assertEquals(-1, Day18(DataFile.Example).part2())
		assertEquals(-1, Day18(DataFile.Part1).part2())
	}
}
