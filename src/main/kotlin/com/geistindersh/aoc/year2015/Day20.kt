package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report
import kotlin.math.sqrt

class Day20(dataFile: DataFile) {
	private val presentsCount = fileToString(2015, 20, dataFile).toInt()

	private fun deliverPresentsInfinite() = sequence {
		for (house in 1..<Int.MAX_VALUE) {
			var presents = house + 1
			val upperBound = sqrt(house.toDouble()).toInt()
			for (elf in 2..upperBound) {
				presents += if (house % elf == 0) elf + house / elf else 0
			}
			presents -= if (upperBound * upperBound == house) upperBound else 0

			yield(Pair(house, presents * 10))
		}
	}

	private fun deliverPresentsLimit50() = sequence {
		for (house in 1..<Int.MAX_VALUE) {
			val presents = (0..50).reduce { presents, elf -> presents + if (house % elf == 0) house / elf else 0 }
			yield(Pair(house, presents * 11))
		}
	}

	fun part1() = deliverPresentsInfinite()
		.takeWhileInclusive { it.second < presentsCount }
		.last()
		.first

	fun part2() = deliverPresentsLimit50()
		.takeWhileInclusive { it.second < presentsCount }
		.last()
		.first
}

fun day20() {
	val day = Day20(DataFile.Part1)
	report(2015, 20, day.part1(), day.part2())
}
