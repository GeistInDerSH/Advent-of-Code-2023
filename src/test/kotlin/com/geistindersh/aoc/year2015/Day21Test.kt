package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21Test {

	@Test
	fun part1() {
		assertEquals(65, Day21(DataFile.Example).part1(8))
	}

	@Test
	fun part2() {
		assertEquals(188, Day21(DataFile.Example).part2(8))
	}
}
