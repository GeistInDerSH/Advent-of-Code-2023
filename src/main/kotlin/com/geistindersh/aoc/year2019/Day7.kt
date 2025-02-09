package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.year2019.intcomputer.IntComputer

class Day7(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val input = fileToString(2019, 7, dataFile).split(',').map(String::toInt)

    private data class ThrusterSequence(
        val instructions: List<Int>,
        val allThrusters: Set<Int>,
        val tried: Set<Int> = emptySet(),
        val value: Int = 0,
    ) {
        fun isFinished() = allThrusters == tried

        fun nextPossibleOptions() =
            (allThrusters - tried)
                .map {
                    val newValue = IntComputer(instructions, listOf(it, value)).run().getOutput()!!
                    this.copy(value = newValue, tried = tried + it)
                }
    }

    override fun part1(): Int {
        val queue = ArrayDeque(listOf(ThrusterSequence(input, FULL_SET)))
        val finished = mutableListOf<ThrusterSequence>()
        while (queue.isNotEmpty()) {
            val head = queue.removeFirst()
            if (head.isFinished()) {
                finished.add(head)
                continue
            }
            queue.addAll(head.nextPossibleOptions())
        }
        return finished.maxOf { it.value }
    }

    override fun part2() = 0

    companion object {
        private val FULL_SET = (0..4).toSet()
    }
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2019, 7, day.part1(), day.part2())
}
