package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day15(
    rawGrid: String,
    rawMoves: String,
) {
    constructor(sections: List<String>) : this(sections.first(), sections.last())
    constructor(dataFile: DataFile) : this(fileToString(2024, 15, dataFile).split("\n\n"))

    private val grid =
        rawGrid
            .split("\n")
            .flatMapIndexed { row, line -> line.mapIndexed { col, char -> Point2D(row, col) to char } }
            .toMap()
            .let { grid ->
                val start = grid.filterValues { it == '@' }.firstNotNullOf { it.key }
                Grid(grid, start)
            }
    private val moves = rawMoves.mapNotNull { Direction.tryFromArrow(it) }

    private data class Grid(
        val grid: Map<Point2D, Char>,
        val position: Point2D,
    ) {
        fun score() = grid.filterValues { it == 'O' }.keys.sumOf { it.row * 100L + it.col }

        fun next(move: Direction): Grid {
            val newPosition = position + move
            val newGrid = grid.toMutableMap()
            return when (grid[newPosition]!!) {
                '.' -> {
                    newGrid[position] = '.'
                    newGrid[newPosition] = '@'
                    Grid(newGrid, newPosition)
                }
                'O' -> {
                    var nextPos = newPosition + move
                    while (nextPos in grid && grid[nextPos]!! == 'O') nextPos += move
                    when (grid[nextPos]!!) {
                        '.' -> {
                            var moving = nextPos
                            while (moving != position) {
                                val prev = moving - move
                                newGrid[moving] = newGrid[prev]!!
                                newGrid[prev] = '.'
                                moving = prev
                            }

                            Grid(newGrid, newPosition)
                        }
                        else -> this
                    }
                }
                else -> this
            }
        }

        @Suppress("unused")
        fun print() {
            val rowMax = grid.keys.maxOf { it.row }
            val colMax = grid.keys.maxOf { it.col }
            val buff = Array(rowMax + 1) { Array(colMax + 1) { ' ' } }
            for ((k, v) in grid) {
                buff[k.row][k.col] = v
            }
            val str = buff.joinToString("\n") { it.joinToString("") }
            println(str)
        }
    }

    fun part1() =
        generateSequence(0 to grid) { (idx, grid) -> if (idx > moves.lastIndex) null else idx + 1 to grid.next(moves[idx]) }
            .takeWhile { true }
            .last()
            .second
            .score()

    fun part2() = 0
}

fun day15() {
    val day = Day15(DataFile.Part1)
    report(2024, 15, day.part1(), day.part2())
}
