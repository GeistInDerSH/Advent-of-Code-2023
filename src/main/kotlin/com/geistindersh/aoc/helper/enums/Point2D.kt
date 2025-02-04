package com.geistindersh.aoc.helper.enums

import kotlin.math.absoluteValue

data class Point2D(
    val row: Int,
    val col: Int,
) {
    operator fun plus(other: Point2D) = Point2D(row + other.row, col + other.col)

    operator fun minus(other: Point2D) = Point2D(row - other.row, col - other.col)

    operator fun plus(other: Direction) = Point2D(row + other.rowInc, col + other.colInc)

    operator fun minus(other: Direction) = Point2D(row - other.rowInc, col - other.colInc)

    operator fun plus(other: Pair<Int, Int>) = Point2D(row + other.first, col + other.second)

    operator fun minus(other: Pair<Int, Int>) = Point2D(row - other.first, col - other.second)

    fun neighbors() = listOf(Direction.North, Direction.East, Direction.South, Direction.West).map { this + it }

    fun neighborsAll() =
        listOf(
            Direction.North.toPair(),
            Direction.East.toPair(),
            Direction.South.toPair(),
            Direction.West.toPair(),
            Direction.North + Direction.East,
            Direction.North + Direction.West,
            Direction.South + Direction.East,
            Direction.South + Direction.West,
        ).map { this + it }

    fun manhattanDistance(other: Point2D) = (row - other.row).absoluteValue + (col - other.col).absoluteValue
}
