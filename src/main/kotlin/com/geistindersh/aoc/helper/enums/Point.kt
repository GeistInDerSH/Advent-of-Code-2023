package com.geistindersh.aoc.helper.enums

data class Point(val row: Int, val col: Int) {
    operator fun plus(other: Point) = Point(row + other.row, col + other.col)
    operator fun plus(other: Direction) = Point(row + other.rowInc, col + other.colInc)
}