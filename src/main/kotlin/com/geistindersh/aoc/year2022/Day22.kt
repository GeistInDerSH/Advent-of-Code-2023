package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day22(dataFile: DataFile) {
    private val grid = fileToStream(2022, 22, dataFile)
        .takeWhile { it.isNotEmpty() }
        .flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, c ->
                if (c == ' ') null
                else Point(row = row, col = col) to c
            }
        }
        .associate { it }
    private val commands = Steps.parseLine(fileToStream(2022, 22, dataFile).last())

    private sealed class Steps {
        data object Left : Steps()
        data object Right : Steps()
        data class Walk(val steps: Int) : Steps()

        companion object {
            private val re = "[0-9]+|[LR]".toRegex()
            fun parseLine(line: String) = re
                .findAll(line)
                .map {
                    when (it.value) {
                        "L" -> Left
                        "R" -> Right
                        else -> Walk(it.value.toInt())
                    }
                }
                .toList()
        }
    }

    private fun walk(fn: (Point, Direction) -> Pair<Point, Direction>): Int {
        var pos = grid
            .keys
            .filter { it.row == 0 }
            .minOf { it.col }
            .let { Point(row = 0, col = it) }
        var direction = Direction.East

        for (cmd in commands) {
            when (cmd) {
                is Steps.Left -> direction = direction.turnLeft()
                is Steps.Right -> direction = direction.turnRight()
                is Steps.Walk -> {
                    val (p, d) = generateSequence(pos to direction) { (p, d) ->
                        val step = p + d
                        when {
                            step in grid && grid[step] == '#' -> Pair(p, d)
                            step !in grid -> {
                                val (wrap, wrapDirection) = fn(p, d)
                                if (grid[wrap] == '.') Pair(wrap, wrapDirection)
                                else Pair(p, d)
                            }

                            else -> Pair(step, d)
                        }
                    }
                        .take(cmd.steps + 1)
                        .last()
                    pos = p
                    direction = d
                }
            }
        }

        val score = when (direction) {
            Direction.North -> 3
            Direction.South -> 1
            Direction.East -> 0
            Direction.West -> 2
        }

        return 1000 * (pos.row + 1) + 4 * (pos.col + 1) + score
    }

    private fun walkMap(point: Point, direction: Direction): Pair<Point, Direction> {
        val newDir = direction.turnRight().turnRight()
        return generateSequence(point) { it + newDir }
            .takeWhile { it in grid }
            .last() to direction
    }


    fun part1() = walk(::walkMap)
    fun part2() = 0
}

fun day22() {
    val day = Day22(DataFile.Part1)
    report(2022, 22, day.part1(), day.part2())
}
