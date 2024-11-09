package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day6(dataFile: DataFile) {
    private val instructions = fileToStream(2015, 6, dataFile).map { Action.from(it) }.toList()

    private sealed class Action(val start: Point2D, val end: Point2D) {
        class Toggle(start: Point2D, end: Point2D) : Action(start, end)
        class Enable(start: Point2D, end: Point2D) : Action(start, end)
        class Disable(start: Point2D, end: Point2D) : Action(start, end)

        fun generateLights() = (start.row..end.row)
            .flatMap { row ->
                (start.col..end.col).map { col -> Point2D(row, col) }
            }.toSet()

        companion object {
            fun from(line: String): Action {
                val words = line.split(' ')
                return if (words[0] == "toggle") {
                    val start = words[1].split(',').map(String::toInt).let { Point2D(it[0], it[1]) }
                    val end = words[3].split(',').map(String::toInt).let { Point2D(it[0], it[1]) }
                    Toggle(start, end)
                } else if (words[1] == "on") {
                    val start = words[2].split(',').map(String::toInt).let { Point2D(it[0], it[1]) }
                    val end = words[4].split(',').map(String::toInt).let { Point2D(it[0], it[1]) }
                    Enable(start, end)
                } else {
                    val start = words[2].split(',').map(String::toInt).let { Point2D(it[0], it[1]) }
                    val end = words[4].split(',').map(String::toInt).let { Point2D(it[0], it[1]) }
                    Disable(start, end)
                }
            }
        }
    }

    private fun followInstructions(): Set<Point2D> {
        val enabledLights = mutableSetOf<Point2D>()
        for (rule in instructions) {
            val lights = rule.generateLights()
            when (rule) {
                is Action.Toggle -> {
                    for (light in lights) {
                        if (light in enabledLights) {
                            enabledLights.remove(light)
                        } else {
                            enabledLights.add(light)
                        }
                    }
                }

                is Action.Disable -> {
                    for (light in lights) {
                        enabledLights.remove(light)
                    }
                }

                is Action.Enable -> {
                    for (light in lights) {
                        enabledLights.add(light)
                    }
                }
            }
        }
        return enabledLights
    }

    private fun followInstructionsBrightness(): Map<Point2D, Int> {
        val enabledLights = mutableMapOf<Point2D, Int>()
        for (rule in instructions) {
            val lights = rule.generateLights()
            when (rule) {
                is Action.Toggle -> {
                    for (light in lights) {
                        val value = enabledLights.getOrDefault(light, 0) + 2
                        enabledLights[light] = value
                    }
                }

                is Action.Disable -> {
                    for (light in lights) {
                        val value = enabledLights.getOrDefault(light, 1)
                        enabledLights[light] = (value - 1).coerceAtLeast(0)
                    }
                }

                is Action.Enable -> {
                    for (light in lights) {
                        enabledLights[light] = enabledLights.getOrDefault(light, 0) + 1
                    }
                }
            }
        }
        return enabledLights
    }

    fun part1() = followInstructions().count()
    fun part2() = followInstructionsBrightness().values.sum()
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2015, 6, day.part1(), day.part2())
}
