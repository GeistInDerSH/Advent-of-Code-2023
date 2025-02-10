package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.permutations
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.year2019.intcomputer.IntComputer
import com.geistindersh.aoc.year2019.intcomputer.Signal

class Day7(
    dataFile: DataFile,
) : AoC<Long, Long> {
    private val input = fileToString(2019, 7, dataFile).split(',').map(String::toLong)

    fun List<Long>.chainedThrusterSignal(): Long =
        this.fold(0) { value, thruster -> IntComputer(input, listOf(thruster, value)).run().getOutput()!! }

    fun List<Long>.chainedThrusterSignalWithFeedback(): Long {
        val thrusters = this.map { IntComputer(input, listOf(it)) }
        thrusters[0].sendInput(0)

        var i = 0
        var output: Long? = null
        while (true) {
            if (output != null) {
                thrusters[i].sendInput(output)
            }

            val signal = thrusters[i].runWhileSignal(Signal.None)
            if (signal == Signal.HasOutput) {
                output = thrusters[i].getOutput()
            } else if (signal == Signal.Halt && i == 4) {
                return output!!
            }

            i = if (i + 1 >= thrusters.size) 0 else i + 1
        }
    }

    override fun part1() = FULL_LOWER_SET.permutations().maxOf { it.chainedThrusterSignal() }

    override fun part2() = FULL_UPPER_SET.permutations().maxOf { it.chainedThrusterSignalWithFeedback() }

    companion object {
        private val FULL_LOWER_SET = (0L..4).toSet()
        private val FULL_UPPER_SET = (5L..9).toSet()
    }
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2019, 7, day.part1(), day.part2())
}
