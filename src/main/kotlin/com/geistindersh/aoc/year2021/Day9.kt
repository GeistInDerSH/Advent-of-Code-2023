package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day9(dataFile: DataFile) {
	private val heightMap = fileToStream(2021, 9, dataFile)
		.flatMapIndexed { row, s ->
			s.mapIndexed { col, c -> Point(row, col) to c.digitToInt() }
		}
		.toMap()
	private val directions = listOf(
		Direction.North,
		Direction.South,
		Direction.East,
		Direction.West,
	)

	private fun Point.neighbors() = directions.map { this + it }

	fun part1() = heightMap
		.entries
		.filter { (point, value) ->
			point
				.neighbors()
				.mapNotNull { heightMap[it] }
				.minBy { it } > value
		}
		.sumOf { it.value + 1 }

	fun part2() = 0
}

fun day9() {
	val day = Day9(DataFile.Part1)
	report(2021, 9, day.part1(), day.part2())
}
