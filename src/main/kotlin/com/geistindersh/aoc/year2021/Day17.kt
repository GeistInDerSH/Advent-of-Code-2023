package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day17(dataFile: DataFile) {
	private val targetArea = "-?[0-9]+"
		.toRegex()
		.findAll(fileToString(2021, 17, dataFile))
		.map { it.value.toInt() }
		.windowed(2, 2) { it.sorted() }
		.map { (start, end) -> (start..end) }
		.toList()
		.let {
			val (colRange, rowRange) = it
			TargetArea(colRange, rowRange)
		}
	private val start = Point(0, 0)

	private data class Throw(val rowVelocity: Int, val colVelocity: Int, val point: Point) {
		fun next(): Throw {
			val x = (rowVelocity - 1).coerceAtLeast(0)
			val y = colVelocity - 1
			val point = Point(point.row + colVelocity, point.col + rowVelocity)
			return Throw(x, y, point)
		}
	}

	private class TargetArea(val colRange: IntRange, val rowRange: IntRange) {
		val minRowVelocity = minimumRowVelocity()

		fun contains(toss: Throw) = toss.point.row in rowRange && toss.point.col in colRange
		fun hasOvershot(toss: Throw): Boolean = toss.point.row < rowRange.first

		fun minimumRowVelocity() = (0..Int.MAX_VALUE)
			.takeWhileInclusive { (0..it).sum() <= colRange.first }
			.last()

		fun possibleRowVelocityRange() = minRowVelocity..colRange.last

		fun maxHeightLandingInBoundsOrNull(point: Point, colVelocity: Int) =
			maxHeightLandingInBoundsOrNull(point, minRowVelocity, colVelocity)

		fun maxHeightLandingInBoundsOrNull(point: Point, rowVelocity: Int, colVelocity: Int): Int? {
			val landed = mutableSetOf<Point>()
			var shot = Throw(rowVelocity, colVelocity, point)
			var landsInArea = false
			while (!hasOvershot(shot)) {
				shot = shot.next()
				landed.add(shot.point)
				if (contains(shot)) landsInArea = true
			}
			return if (landsInArea) landed.maxOf { it.row } else null
		}
	}


	private fun findShotLandingInTargetArea(): Int {
		var max = Int.MIN_VALUE
		for (colVelocity in 0..targetArea.colRange.last * 2) {
			val height = targetArea.maxHeightLandingInBoundsOrNull(start, colVelocity) ?: continue
			max = maxOf(max, height)
		}
		return max
	}

	fun part1() = findShotLandingInTargetArea()

	fun part2(): Int {
		val landingInBounds = mutableSetOf<Throw>()

		for (rowVelocity in targetArea.possibleRowVelocityRange()) {
			for (colVelocity in targetArea.rowRange.first..targetArea.colRange.last * 2) {
				targetArea.maxHeightLandingInBoundsOrNull(start, rowVelocity, colVelocity) ?: continue
				landingInBounds.add(Throw(rowVelocity, colVelocity, start))
			}
		}

		return landingInBounds.size
	}
}

fun day17() {
	val day = Day17(DataFile.Part1)
	report(2021, 17, day.part1(), day.part2())
}
