package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.cycle
import com.geistindersh.aoc.helper.math.positiveModulo
import com.geistindersh.aoc.helper.report

class Day20(dataFile: DataFile) {
	private val data = fileToStream(2022, 20, dataFile).map(String::toLong).toList()
	private val dataIndices = data.indices.toList()

	private fun getUpdatedPositions(data: List<Long>, positions: List<Int>, indices: List<Int>): List<Long> {
		val values = positions.map(Int::toLong).toMutableList()
		val div = data.size - 1

		for (index in indices) {
			val prevIndex = values[index]

			// I originally wrote this in Clojure, which handles modulo of negative values
			// differently than Kotlin. Because of that, replicate the behavior here
			val newIndex = (prevIndex + data[index]).positiveModulo(div)

			for (i in values.indices) {
				values[i] = when (val it = values[i]) {
					in (prevIndex..newIndex) -> it - 1
					in (newIndex..prevIndex) -> it + 1
					else -> it
				}
			}
			values[index] = newIndex
		}
		return values
	}

	private fun getValues(data: List<Long>, positions: List<Long>): List<Long> {
		val zip = positions.zip(data)
		val values = data.toMutableList()
		for (z in zip) {
			values[z.first.toInt()] = z.second
		}
		return values
	}

	private fun solution(values: List<Long>): Long {
		return values
			.cycle()
			.dropWhile { it != 0L }
			.filterIndexed { idx, _ -> idx % 1000 == 0 }
			.take(4)
			.reduce(Long::plus)
	}

	fun part1(): Long {
		val positions = getUpdatedPositions(data, dataIndices, dataIndices)
		val values = getValues(data, positions)
		return solution(values)
	}

	fun part2(): Long {
		val data = data.map { it * 811589153 }
		val indices = (0..<10).flatMap { dataIndices }
		val positions = getUpdatedPositions(data, dataIndices, indices)
		val values = getValues(data, positions)
		return solution(values)
	}
}

fun day20() {
	val day = Day20(DataFile.Part1)
	report(2022, 20, day.part1(), day.part2())
}
