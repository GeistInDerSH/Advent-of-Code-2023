package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day18Test {

	@Test
	fun part1() {
		assertEquals(4, Day18(DataFile.Example).part1(4))
	}

	@Test
	fun part2() {
		assertEquals(17, Day18(DataFile.Example).part2(5))
	}
}
