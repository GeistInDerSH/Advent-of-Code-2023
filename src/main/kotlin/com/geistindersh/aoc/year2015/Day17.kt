package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.subsetSum
import com.geistindersh.aoc.helper.report

class Day17(dataFile: DataFile) {
	private val numbers = fileToStream(2015, 17, dataFile).map(String::toInt).toList()

	fun part1(target: Int) = numbers.subsetSum(target).count()

	fun part2(target: Int): Int {
		val sums = numbers.subsetSum(target).sortedBy { it.size }
		val min = sums.minOf { it.size }
		return sums.filter { it.size == min }.count()
	}
}

fun day17() {
	val day = Day17(DataFile.Part1)
	report(2015, 17, day.part1(150), day.part2(150))
}
