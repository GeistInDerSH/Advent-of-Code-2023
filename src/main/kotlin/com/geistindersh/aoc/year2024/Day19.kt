package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import java.util.PriorityQueue

class Day19(
    private val patterns: Set<String>,
    private val designs: List<String>,
) : AoC<Int, Long> {
    constructor(dataFile: DataFile) : this(fileToString(2024, 19, dataFile))
    constructor(input: String) : this(
        input
            .substringBefore("\n")
            .split(',')
            .map { it.trim() }
            .sortedByDescending { it.length }
            .toSet(),
        input.substringAfter("\n").split("\n").drop(1),
    )

    private fun String.isValid(): Boolean {
        if (this.isEmpty()) return true

        val queue = PriorityQueue<String>(compareBy { it.length }).apply { add(this@isValid) }
        val seen = mutableSetOf<String>()
        while (queue.isNotEmpty()) {
            val head = queue.poll()
            if (head.isBlank()) return true
            if (!seen.add(head)) continue
            if (head in patterns) return true

            for (pattern in patterns) {
                if (!head.startsWith(pattern)) continue
                val subStr = head.substringAfter(pattern)
                queue.add(subStr)
            }
        }
        return false
    }

    private fun String.validComboCount(): Long {
        val queue = PriorityQueue<String>(compareByDescending { it.length }).apply { add(this@validComboCount) }
        val memory = mutableMapOf<String, Long>(this to 1L)

        while (queue.isNotEmpty()) {
            val head = queue.poll()

            for (pattern in patterns) {
                if (!head.startsWith(pattern)) continue
                val substring = head.substringAfter(pattern)
                if (substring !in memory) {
                    queue.add(substring)
                }
                memory[substring] = memory.getOrDefault(substring, 0L) + memory[head]!!
            }
        }
        return memory.getOrDefault("", 0L)
    }

    override fun part1() = designs.count { it.isValid() }

    override fun part2() = designs.filter { it.isValid() }.sumOf { it.validComboCount() }
}

fun day19() {
    val day = Day19(DataFile.Part1)
    report(2024, 19, day.part1(), day.part2())
}
