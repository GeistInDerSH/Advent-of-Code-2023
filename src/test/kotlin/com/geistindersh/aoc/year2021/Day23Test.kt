package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day23Test {

	@Test
	fun part1() {
		assertEquals(12521, Day23(DataFile.Example).part1())
	}

	@Test
	fun part2() {
		assertEquals(-1, Day23(DataFile.Example).part2())
		assertEquals(-1, Day23(DataFile.Part1).part2())
	}
}
