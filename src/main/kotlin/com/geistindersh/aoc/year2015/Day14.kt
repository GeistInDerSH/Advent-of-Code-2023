package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day14(dataFile: DataFile) {
    private val data =
        fileToStream(2015, 14, dataFile)
            .map { line ->
                val name = line.substringBefore(" ")
                val (speed, time, sleep) = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
                Reindeer(name, speed, time, sleep)
            }
            .toList()

    data class Reindeer(val name: String, val speed: Int, val time: Int, val sleep: Int) {
        fun isResting(round: Int): Boolean {
            if (round < time) return false
            return (round - time) % (time + sleep) < sleep
        }
    }

    data class Round(val round: Int, val reindeer: Collection<Reindeer>, val distances: Map<String, Int>) {
        fun next(): Round {
            val newDistances =
                distances
                    .toMutableMap()
                    .also {
                        reindeer.forEach { deer ->
                            val speed = if (deer.isResting(round)) 0 else deer.speed
                            it[deer.name] = it[deer.name]!! + speed
                        }
                    }
            return Round(round + 1, reindeer, newDistances)
        }
    }

    private fun fly() = generateSequence(Round(1, data, data.associate { it.name to it.speed })) { it.next() }

    fun part1(seconds: Int) = fly().drop(seconds - 1).first().distances.maxOf { it.value }

    fun part2(seconds: Int) =
        fly()
            .take(seconds)
            .flatMap { round ->
                val max = round.distances.maxBy { it.value }.value
                round.distances.filter { it.value == max }.toList()
            }
            .groupingBy { it.first }
            .eachCount()
            .maxOf { it.value }
}

fun day14() {
    val day = Day14(DataFile.Part1)
    report(2015, 14, day.part1(2503), day.part2(2503))
}
