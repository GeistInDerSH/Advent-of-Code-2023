package com.geistindersh.aoc.year2023.day8

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.math.lcm
import com.geistindersh.aoc.helper.report

data class Walk(val instructions: String, val nodes: Map<String, Pair<String, String>>) {
    fun getStepsCount(start: String, end: String): Long {
        var count = 0L
        var index = 0
        var key = start
        while (!key.endsWith(end)) {
            val isLeft = instructions[index] == 'L'
            val pair = nodes[key]!!
            key = if (isLeft) pair.first else pair.second
            index = (index + 1) % instructions.length
            count += 1
        }
        return count
    }
}

fun parseInput(fileName: String): Walk {
    val lines = fileToStream(fileName).toList()
    val instructions = lines.first()

    val nodes = lines.drop(2).associate {
        val key = it.substringBefore(' ')
        val (left, right) = it.substringAfter('(').substringBefore(')').split(", ")
        key to Pair(left, right)
    }

    return Walk(instructions, nodes)
}

/**
 * @return The number of steps to get from "AAA" to "ZZZ"
 */
fun part1(walk: Walk) = walk.getStepsCount("AAA", "ZZZ")

/**
 * @return The minimum number of steps to get from all nodes ending in "A" to all ending in "Z"
 */
fun part2(walk: Walk): Long {
    return walk
        .nodes
        .keys
        .filter { it.endsWith('A') }
        .map { walk.getStepsCount(it, "Z") }
        .lcm()
}

fun day8() {
    val input = parseInput(DataFile.Part1.filePath(8))
    report(
        dayNumber = 8,
        part1 = part1(input),
        part2 = part2(input),
    )
}
