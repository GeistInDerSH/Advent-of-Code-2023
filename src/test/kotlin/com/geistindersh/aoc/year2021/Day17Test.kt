package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17Test {

	@Test
	fun part1() {
		assertEquals(45, Day17(DataFile.Example).part1())
	}

	@Test
	fun part2() {
		assertEquals(112, Day17(DataFile.Example).part2())
	}
}
