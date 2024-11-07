package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.absoluteValue

class Day12(dataFile: DataFile) {
    private val actions = fileToStream(2020, 12, dataFile)
        .map { Action.from(it) }
        .toList()

    private sealed class Action(open val value: Int) {
        data class North(override val value: Int) : Action(value)
        data class South(override val value: Int) : Action(value)
        data class West(override val value: Int) : Action(value)
        data class East(override val value: Int) : Action(value)
        data class Forward(override val value: Int) : Action(value)
        data class Left(override val value: Int) : Action(value) {
            val turns = value / 90
        }

        data class Right(override val value: Int) : Action(value) {
            val turns = value / 90
        }

        companion object {
            fun from(line: String): Action {
                val action = line.first()
                val value = line.substring(1).toInt()
                return when (action) {
                    'N' -> North(value)
                    'S' -> South(value)
                    'W' -> West(value)
                    'E' -> East(value)
                    'L' -> Left(value)
                    'R' -> Right(value)
                    'F' -> Forward(value)
                    else -> throw IllegalArgumentException("Unrecognised action $action")
                }
            }
        }
    }

    private class Ship(private val actions: List<Action>) {
        private var direction = Direction.East
        private var position = Point(0, 0)

        fun distance() = position.row.absoluteValue + position.col.absoluteValue

        fun navigate() {
            for (action in actions) {
                when (action) {
                    is Action.Forward -> {
                        for (i in 0..<action.value) {
                            position += direction
                        }
                    }

                    is Action.Left -> {
                        for (i in 0..<action.turns) {
                            direction = direction.turnLeft()
                        }
                    }

                    is Action.Right -> {
                        for (i in 0..<action.turns) {
                            direction = direction.turnRight()
                        }
                    }

                    is Action.North -> position = position.copy(row = position.row - action.value)
                    is Action.South -> position = position.copy(row = position.row + action.value)
                    is Action.East -> position = position.copy(col = position.col + action.value)
                    is Action.West -> position = position.copy(col = position.col - action.value)
                }
            }
        }
    }

    fun part1() = Ship(actions).let {
        it.navigate()
        it.distance()
    }

    fun part2() = 0
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2020, 12, day.part1(), day.part2())
}
