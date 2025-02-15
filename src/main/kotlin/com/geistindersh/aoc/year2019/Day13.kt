package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.year2019.intcomputer.IntComputer
import com.geistindersh.aoc.year2019.intcomputer.Signal

class Day13(
    dataFile: DataFile,
) : AoC<Int, Long> {
    private val instructions = fileToString(2019, 13, dataFile).split(",").map(String::toLong)

    private enum class TileId {
        Empty,
        Wall,
        Block,
        HorizontalPaddle,
        Ball,
        ;

        companion object {
            fun from(i: Long) = from(i.toInt())

            fun from(i: Int) =
                when (i) {
                    0 -> Empty
                    1 -> Wall
                    2 -> Block
                    3 -> HorizontalPaddle
                    4 -> Ball
                    else -> throw IllegalArgumentException("$i is not a valid tile ID")
                }
        }
    }

    private fun Map<Point2D, TileId>.debug() {
        val chars = Array(21) { CharArray(40) { ' ' } }
        for ((p, v) in this) {
            val c =
                when (v) {
                    TileId.Empty -> ' '
                    TileId.Wall -> '|'
                    TileId.Ball -> '0'
                    TileId.Block -> '#'
                    TileId.HorizontalPaddle -> '^'
                }
            chars[p.row][p.col] = c
        }
        println(chars.joinToString("\n") { it.joinToString("") })
        println()
    }

    override fun part1(): Int {
        val computer = IntComputer(instructions)
        val screen = mutableMapOf<Point2D, TileId>()

        while (true) {
            computer.runWhileSignal(Signal.None)
            val col = computer.getOutput()
            computer.runWhileSignal(Signal.None)
            val row = computer.getOutput()
            computer.runWhileSignal(Signal.None)
            val tileId = computer.getOutput()

            if (col == null || row == null || tileId == null) break
            val point = Point2D(row.toInt(), col.toInt())
            screen[point] = TileId.from(tileId)
        }
        return screen.count { it.value == TileId.Block }
    }

    override fun part2(): Long {
        val screen = mutableMapOf<Point2D, TileId>()
        val computer = IntComputer(instructions.toMutableList().apply { this[0] = 2 })
        computer.runUntilSignal(Signal.NeedsInput)

        var score = 0L
        var ballColumn = 0L
        var paddleColumn = 0L
        while (true) {
            val paddleValue = ballColumn.compareTo(paddleColumn).toLong()
            computer.sendInput(paddleValue)

            val output = mutableListOf<Long>()
            var signal = Signal.None
            // If we get a Halt, don't return the current score, run through the last bit of output first
            while (signal != Signal.NeedsInput && signal != Signal.Halt) {
                signal = computer.runWhileSignal(Signal.None)
                if (signal == Signal.HasOutput) {
                    output.add(computer.getOutput()!!)
                }
            }

            for ((col, row, tileId) in output.windowed(3, 3)) {
                if (col == -1L && row == 0L) {
                    score = tileId
                    continue
                }

                val point = Point2D(row.toInt(), col.toInt())
                val tid = TileId.from(tileId)
                screen[point] = tid
                when (tid) {
                    TileId.Ball -> ballColumn = col
                    TileId.HorizontalPaddle -> paddleColumn = col
                    else -> continue
                }
            }
            if (signal == Signal.Halt) break
        }
        return score
    }
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2019, 13, day.part1(), day.part2())
}
