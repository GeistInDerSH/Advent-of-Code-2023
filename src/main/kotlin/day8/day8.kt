package day8

import helper.fileToStream
import helper.report
import kotlin.math.max

data class Walk(val instructions: String, val nodes: Map<String, Pair<String, String>>)

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

fun part1(walk: Walk): Int {
    var key = "AAA"
    var count = 0
    var index = 0
    while (key != "ZZZ") {
        val pair = walk.nodes[key]!!
        key = if (walk.instructions[index] == 'L') {
            pair.first
        } else {
            pair.second
        }
        count += 1
        index = if (index + 1 == walk.instructions.length) 0 else index + 1
    }
    return count
}

fun lcm(a: Long, b: Long): Long {
    val larger = max(a, b)
    val maxLCM = a * b
    var lcm = larger
    while (lcm <= maxLCM) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLCM
}

fun part2(walk: Walk): Long {
    val terminationCount = walk.nodes.keys.filter { it.endsWith('A') }.parallelStream().map {
        var count = 0L
        var index = 0
        var key = it
        while (!key.endsWith('Z')) {
            val isLeft = walk.instructions[index] == 'L'
            val pair = walk.nodes[key]!!
            key = if (isLeft) pair.first else pair.second
            index = if (index + 1 == walk.instructions.length) 0 else index + 1
            count += 1
        }
        count
    }.toList()
    var result = terminationCount[0]
    terminationCount.drop(1).forEach { result = lcm(result, it) }
    return result
}

fun day8() {
    val input = parseInput("src/main/resources/day_8/part_1.txt")
    report(
        dayNumber = 8,
        part1 = part1(input),
        part2 = part2(input),
    )
}
