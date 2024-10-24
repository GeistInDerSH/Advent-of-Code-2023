package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day5(dataFile: DataFile) {
	private val lines = fileToStream(2021, 5, dataFile)
		.map { line ->
			val (start, end) = "[0-9]+".toRegex()
				.findAll(line)
				.map { it.value.toInt() }
				.windowed(2, 2)
				.map { Point(it[0], it[1]) }
				.toList()
			val (minRow, maxRow) = if (start.row < end.row) start.row to end.row else end.row to start.row
			val (minCol, maxCol) = if (start.col < end.col) start.col to end.col else end.col to start.col
			Point(minRow, minCol) to Point(maxRow, maxCol)
		}
		.toList()

	private fun List<Pair<Point, Point>>.fillInLines() = this
		.flatMap { (start, end) ->
			(start.row..end.row)
				.flatMap { row ->
					(start.col..end.col).map { col -> Point(row, col) }
				}
		}

	fun part1() = lines
		.filter { (start, end) -> start.row == end.row || start.col == end.col }
		.fillInLines()
		.groupingBy { it }
		.eachCount()
		.count { it.value > 1 }

	fun part2() = 0
}

fun day5() {
	val day = Day5(DataFile.Part1)
	report(2021, 5, day.part1(), day.part2())
}
