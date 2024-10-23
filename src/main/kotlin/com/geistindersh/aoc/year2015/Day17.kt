package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day17(dataFile: DataFile) {
	private val numbers = fileToStream(2015, 17, dataFile).map(String::toInt).toList()

	private fun List<Int>.subsetSum(target: Int) = subsetSum(this, target, emptyList())
	private fun subsetSum(numbers: List<Int>, target: Int, partial: List<Int>): Sequence<List<Int>> =
		sequence {
			val partialSum = if (partial.isEmpty()) 0 else partial.reduce(Int::plus)
			if (partialSum == target) yield(partial)
			if (partialSum > target) return@sequence
			numbers.forEachIndexed { index, i ->
				val rem = numbers.drop(index + 1)
				yieldAll(subsetSum(rem, target, partial + i))
			}
		}

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
