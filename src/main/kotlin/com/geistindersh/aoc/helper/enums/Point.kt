package com.geistindersh.aoc.helper.enums

data class Point(val row: Int, val col: Int) {
	operator fun plus(other: Point) = Point(row + other.row, col + other.col)
	operator fun plus(other: Direction) = Point(row + other.rowInc, col + other.colInc)
	operator fun plus(other: Pair<Int, Int>) = Point(row + other.first, col + other.second)

	fun neighbors() = listOf(Direction.North, Direction.East, Direction.South, Direction.West).map { this + it }
	fun neighborsAll() = listOf(
		Direction.North.pair(),
		Direction.East.pair(),
		Direction.South.pair(),
		Direction.West.pair(),
		Direction.North + Direction.East,
		Direction.North + Direction.West,
		Direction.South + Direction.East,
		Direction.South + Direction.West,
	).map { this + it }
}