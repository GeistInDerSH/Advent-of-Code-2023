package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day2(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val commands =
        fileToStream(2021, 2, dataFile)
            .map { line ->
                val (cmd, dist) = line.split(" ")
                when (cmd) {
                    "forward" -> Command.Forward(dist.toInt())
                    "down" -> Command.Down(dist.toInt())
                    "up" -> Command.Up(dist.toInt())
                    else -> throw IllegalArgumentException("Unrecognised command: $cmd")
                }
            }.toList()

    private sealed class Command(
        open val distance: Int,
    ) {
        data class Forward(
            override val distance: Int,
        ) : Command(distance)

        data class Down(
            override val distance: Int,
        ) : Command(distance)

        data class Up(
            override val distance: Int,
        ) : Command(distance)
    }

    override fun part1() =
        commands
            .fold(Pair(0, 0)) { (dep, fwd), cmd ->
                when (cmd) {
                    is Command.Forward -> dep to (fwd + cmd.distance)
                    is Command.Down -> (dep + cmd.distance) to fwd
                    is Command.Up -> (dep - cmd.distance) to fwd
                }
            }.toList()
            .reduce(Int::times)

    override fun part2() =
        commands
            .fold(Triple(0, 0, 0)) { (dep, fwd, aim), cmd ->
                when (cmd) {
                    is Command.Forward -> Triple(dep + cmd.distance * aim, fwd + cmd.distance, aim)
                    is Command.Down -> Triple(dep, fwd, aim + cmd.distance)
                    is Command.Up -> Triple(dep, fwd, aim - cmd.distance)
                }
            }.toList()
            .dropLast(1)
            .reduce(Int::times)
}

fun day2() {
    val day = Day2(DataFile.Part1)
    report(2021, 2, day.part1(), day.part2())
}
