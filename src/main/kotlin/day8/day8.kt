package day8

import helper.files.DataFile
import helper.files.fileToStream
import helper.report
import kotlin.math.max

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
 * @param a The first value
 * @param b The second value
 * @return The least common multiple of [a] and [b]
 */
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

/**
 * @return The number of steps to get from "AAA" to "ZZZ"
 */
fun part1(walk: Walk) = walk.getStepsCount("AAA", "ZZZ")

/**
 * @return The minimum number of steps to get from all nodes ending in "A" to all ending in "Z"
 */
fun part2(walk: Walk) = walk.nodes.keys.filter { it.endsWith('A') }.map { walk.getStepsCount(it, "Z") }
    .fold(1L) { acc, l -> lcm(acc, l) }

fun day8() {
    val input = parseInput(DataFile.Part1.filePath(8))
    report(
        dayNumber = 8,
        part1 = part1(input),
        part2 = part2(input),
    )
}
