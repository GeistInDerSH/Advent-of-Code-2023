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
) : AoC<Int, Int> {
    private val data = fileToString(2019, 13, dataFile).split(",").map(String::toLong)

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

    override fun part1(): Int {
        val computer = IntComputer(data)
        val screen = mutableMapOf<Point2D, TileId>()

        while (true) {
            computer.runWhileSignal(Signal.None)
            val col = computer.getOutput()
            computer.runWhileSignal(Signal.None)
            val row = computer.getOutput()
            computer.runWhileSignal(Signal.None)
            val tileId = computer.getOutput()
            if (col == null || row == null || tileId == null) {
                break
            }
            val point = Point2D(row.toInt(), col.toInt())
            screen[point] = TileId.from(tileId)
        }
        return screen.count { it.value == TileId.Block }
    }

    override fun part2() = 0
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2019, 13, day.part1(), day.part2())
}
