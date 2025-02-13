package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.year2019.intcomputer.IntComputer
import com.geistindersh.aoc.year2019.intcomputer.Signal

class Day11(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val instructions = fileToString(2019, 11, dataFile).split(",").map(String::toLong)

    private fun List<Long>.simulate() =
        buildMap {
            val computer = IntComputer(this@simulate)
            var direction = Direction.North
            var position = Point2D(0, 0)

            var signal = Signal.None
            while (signal != Signal.Halt) {
                computer.sendInput(this.getOrDefault(position, 0L))
                // Write the Paint Color
                computer.runWhileSignal(Signal.None)
                val o1 = computer.getOutput()
                // Get the updated direction
                signal = computer.runWhileSignal(Signal.None)
                val o2 = computer.getOutput()
                if (o1 == null || o2 == null) {
                    break
                }
                this[position] = o1
                direction =
                    when (o2) {
                        0L -> direction.turnLeft()
                        1L -> direction.turnRight()
                        else -> throw Exception("Unexpected output: $o2")
                    }
                position += direction
            }
        }

    override fun part1() =
        instructions
            .simulate()
            .let {
                println(it)
                it
            }.count()

    override fun part2() = 0
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2019, 11, day.part1(), day.part2())
}
