package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.permutations
import com.geistindersh.aoc.helper.report

class Day13(dataFile: DataFile) {
    private val data =
        fileToStream(2015, 13, dataFile)
            .map { line ->
                val parts = line.split(" ")
                val person1 = parts[0]
                val person2 = parts[parts.lastIndex].replace(".", "")
                val sign = if (parts[2] == "lose") -1 else 1
                val happiness = parts[3].toInt()
                (person1 to person2) to (sign * happiness)
            }
            .toMap()
    private val people = data.flatMap { it.key.toList() }.toSet()

    private fun optimalSeatingHappiness(
        people: Set<String>,
        happinessMap: Map<Pair<String, String>, Int>,
    ) = people
        .permutations()
        .maxOf { group ->
            group.foldIndexed(0) { index, acc, us ->
                val left = if (index - 1 < 0) group.last() else group[index - 1]
                val right = if (index + 1 > group.lastIndex) group.first() else group[index + 1]
                acc + happinessMap[us to left]!! + happinessMap[us to right]!!
            }
        }

    fun part1() = optimalSeatingHappiness(people, data)

    fun part2(): Int {
        val us = "Us"
        val data =
            data.toMutableMap().also {
                people.forEach { person ->
                    it[person to us] = 0
                    it[us to person] = 0
                }
            }
        return optimalSeatingHappiness(people + setOf(us), data)
    }
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2015, 13, day.part1(), day.part2())
}
