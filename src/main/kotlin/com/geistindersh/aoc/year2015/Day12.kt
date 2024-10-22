package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day12(dataFile: DataFile) {
	private val data = fileToString(2015, 12, dataFile)

	fun part1() = "-?[0-9]+".toRegex().findAll(data).map { println(it.value); it }.sumOf { it.value.toInt() }
	fun part2() = 0
}

fun day12() {
	val day = Day12(DataFile.Part1)
	report(2015, 12, day.part1(), day.part2())
}
