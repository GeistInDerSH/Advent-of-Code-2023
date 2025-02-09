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

    fun Pair<Set<Int>, Int>.foo(): List<Pair<Set<Int>, Int>> {
        val (tried, value) = this
        val toTry = FULL_SET - tried
        return toTry.map {
            val v = IntComputer(input, listOf(it, value)).run().getOutput()!!
            (tried + it) to v
        }
    }

    override fun part1(): Int {
        val queue = ArrayDeque(listOf(emptySet<Int>() to 0))
        val finished = mutableListOf<Pair<Set<Int>, Int>>()
        while (queue.isNotEmpty()) {
            val head = queue.removeFirst()
            if (head.first == FULL_SET) {
                finished.add(head)
                continue
            }
            val toAdd = head.foo()
            queue.addAll(toAdd)
        }
        return finished.maxOf { it.second }
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
