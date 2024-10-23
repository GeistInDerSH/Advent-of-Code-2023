package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day15Test {

	@Test
	fun part1() {
		assertEquals(62842880, Day15(DataFile.Example).part1())
	}

	@Test
	fun part2() {
		assertEquals(57600000, Day15(DataFile.Example).part2())
	}
}
