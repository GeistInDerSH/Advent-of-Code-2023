package day2

import helper.fileToStream
import helper.report

fun part1(fileName: String): Int {
    val cubeCounts = mapOf("red" to 12, "blue" to 14, "green" to 13)

    return fileToStream(fileName).mapIndexed { index, s ->
        val i = index + 1
        val isValid = s.replace("Game ${i}: ", "").split(";").all { sections ->
            sections.trim().split(",").all {
                val parts = it.trim().split(" ")
                val name = parts.last()
                val count = parts.first().toInt()
                name in cubeCounts && cubeCounts[name]!! >= count
            }
        }

        if (isValid) {
            i
        } else {
            0
        }
    }.toList().sum()
}

fun part2(fileName: String): Int {
    return fileToStream(fileName).mapIndexed { index, s ->
        val i = index + 1
        val minCubeCountByColor = listOf("red", "blue", "green").associateWith { 0 }.toMutableMap()
        s.replace("Game ${i}: ", "").split(";").forEach { sections ->
            sections.trim().split(",").forEach {
                val parts = it.trim().split(" ")
                val name = parts.last()
                val count = parts.first().toInt()
                if (name in minCubeCountByColor && minCubeCountByColor[name]!! < count) {
                    minCubeCountByColor[name] = count
                }
            }
        }
        minCubeCountByColor.values.fold(1) { acc: Int, idx: Int -> acc * idx }
    }.toList().sum()
}

fun day2() {
    report(
        dayNumber = 2,
        part1 = part1("src/main/resources/day_2/part_1.txt"),
        part2 = part2("src/main/resources/day_2/part_1.txt")
    )
}