package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day18(dataFile: DataFile) {
	private val points = fileToStream(2015, 18, dataFile)
		.flatMapIndexed { row, line ->
			line.mapIndexedNotNull { col, c -> Point(row, col) to c }
		}
		.toMap()

	private val directions = listOf(
		Direction.North.pair(),
		Direction.East.pair(),
		Direction.South.pair(),
		Direction.West.pair(),
		Direction.North + Direction.East,
		Direction.North + Direction.West,
		Direction.South + Direction.East,
		Direction.South + Direction.West,
	)

	private fun Point.neighbors() = directions.map { this + it }

	private fun Map<Point, Char>.gameOfLife() = generateSequence(this) { currentMap ->
		currentMap.entries.associate { (k, v) ->
			val enabledNeighbors = k.neighbors().mapNotNull { currentMap[it] }.count { it == '#' }

			when {
				v == '#' && enabledNeighbors == 2 -> k to '#'
				v == '#' && enabledNeighbors == 3 -> k to '#'
				v == '.' && enabledNeighbors == 3 -> k to '#'
				else -> k to '.'
			}
		}
	}

	private fun Map<Point, Char>.print() {
		val rows = this.keys.maxOf { it.row }
		val cols = this.keys.maxOf { it.col }
		for (row in 0..rows) {
			for (col in 0..cols) {
				print(this[Point(row, col)]!!)
			}
			println()
		}
		println()
	}

	fun part1(steps: Int) = points
		.gameOfLife()
		.drop(steps)
		.first()
		.count { it.value == '#' }

	fun part2() = 0
}

fun day18() {
	val day = Day18(DataFile.Part1)
	report(2015, 18, day.part1(100), day.part2())
}
