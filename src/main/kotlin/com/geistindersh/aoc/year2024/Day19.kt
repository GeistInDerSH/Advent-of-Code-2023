package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import java.util.PriorityQueue

class Day19(
    private val patterns: Set<String>,
    private val designs: List<String>,
    private val debug: Boolean = false,
) {
    constructor(dataFile: DataFile, debug: Boolean = false) : this(fileToString(2024, 19, dataFile), debug)
    constructor(input: String, debug: Boolean = false) : this(
        input
            .substringBefore("\n")
            .split(',')
            .map { it.trim() }
            .sortedByDescending { it.length }
            .toSet(),
        input.substringAfter("\n").split("\n").drop(1),
        debug,
    )

    private fun String.isValid(): Boolean {
        if (this.isEmpty()) return true
        if (debug) println(this)

        val queue = PriorityQueue<String>(compareBy { it.length }).apply { add(this@isValid) }
        val seen = mutableSetOf<String>()
        while (queue.isNotEmpty()) {
            val head = queue.poll()
            if (head.isBlank()) return true
            if (!seen.add(head)) continue
            if (head in patterns) return true
            if (debug) println(head)

            for (pattern in patterns) {
                if (!head.startsWith(pattern)) continue
                val subStr = head.substringAfter(pattern)
                queue.add(subStr)
            }
        }
        return false
    }

    init {
        println()
    }

    fun part1() =
        designs.count {
            val v = it.isValid()
            println("$it: $v")
            v
        }

    fun part2() = 0
}

fun day19() {
    val day = Day19(DataFile.Part1)
    report(2024, 19, day.part1(), day.part2())
}
