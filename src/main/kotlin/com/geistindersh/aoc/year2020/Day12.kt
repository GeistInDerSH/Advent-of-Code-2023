package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.absoluteValue

class Day12(dataFile: DataFile) {
    private val ship = fileToStream(2020, 12, dataFile)
        .map { Action.from(it) }
        .toList()
        .let { Ship(it) }

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
        fun navigate(): Int {
            var direction = Direction.East
            var position = Point(0, 0)

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

            return position.row.absoluteValue + position.col.absoluteValue
        }

        fun navigateWithWaypoint(): Int {
            var position = Point(0, 0)
            var waypoint = Point(-1, 10)

            for (action in actions) {
                when (action) {
                    is Action.Forward -> {
                        for (i in 0..<action.value) {
                            position += waypoint
                        }
                    }

                    is Action.Left -> {
                        for (i in 0..<action.turns) {
                            waypoint = Point(-1 * waypoint.col, waypoint.row)
                        }
                    }

                    is Action.Right -> {
                        for (i in 0..<action.turns) {
                            waypoint = Point(waypoint.col, -1 * waypoint.row)
                        }
                    }

                    is Action.North -> waypoint = waypoint.copy(row = waypoint.row - action.value)
                    is Action.South -> waypoint = waypoint.copy(row = waypoint.row + action.value)
                    is Action.East -> waypoint = waypoint.copy(col = waypoint.col + action.value)
                    is Action.West -> waypoint = waypoint.copy(col = waypoint.col - action.value)
                }
            }

            return position.row.absoluteValue + position.col.absoluteValue
        }
    }

    fun part1() = ship.navigate()

    fun part2() = ship.navigateWithWaypoint()
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2020, 12, day.part1(), day.part2())
}
