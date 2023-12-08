package day8

import helper.fileToStream
import helper.report

data class Walk(val instructions: String, val nodes: Map<String, Pair<String, String>>)

fun parseInput(fileName: String): Walk {
    val lines = fileToStream(fileName).toList()
    val instructions = lines.first()

    val nodes = lines.drop(2).associate {
        val parts = it.split(' ')
        val key = parts[0]
        val left = parts[2].substringAfter('(').substringBefore(',')
        val right = parts[3].substringBefore(')')
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

fun day8() {
    val input = parseInput("src/main/resources/day_8/part_1.txt")
    println(input)
    report(
        dayNumber = 8,
        part1 = part1(input),
        part2 = "",
    )
}
