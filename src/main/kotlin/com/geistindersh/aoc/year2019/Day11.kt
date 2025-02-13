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
) : AoC<Int, String> {
    private val instructions = fileToString(2019, 11, dataFile).split(",").map(String::toLong)

    private fun List<Long>.simulate(initialState: Long) =
        buildMap {
            val computer = IntComputer(this@simulate)
            var direction = Direction.North
            var position = Point2D(0, 0)

            var signal = Signal.None
            while (signal != Signal.Halt) {
                val inputColor = if (this.isEmpty()) initialState else this.getOrDefault(position, 0L)
                computer.sendInput(inputColor)
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

    override fun part1() = instructions.simulate(0).count()

    override fun part2() =
        instructions.simulate(1).let {
            val maxRow = it.keys.maxOf { it.row }
            val maxCol = it.keys.maxOf { it.col }
            val outputBuffer = Array(maxRow + 1) { CharArray(maxCol + 1) { ' ' } }
            for ((point, color) in it) {
                val (row, col) = point
                outputBuffer[row][col] = if (color == 0L) ' ' else '#'
            }
            outputBuffer.joinToString("\n") { it.joinToString("") }
        }
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2019, 11, day.part1(), day.part2())
}
