package com.geistindersh.aoc.year2023.day6

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

fun parseInput(fileType: DataFile): Map<Long, Long> {
    val (times, distance) = fileToStream(2023, 6, fileType)
        .map { line ->
            line.substringAfter(':')
                .trim()
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toLong() }
        }
        .toList()
    return times.zip(distance).associate { it.first to it.second }
}

/**
 * Calculate the number of possible winning moves that beat the [recordDistance] within the [raceTime]
 *
 * @param raceTime The time in ms for how long the race will go
 * @param recordDistance The distance to beat
 * @return The number of winning plays to make
 */
fun getPossibleWinCount(raceTime: Long, recordDistance: Long): Long {
    return (0..raceTime)
        .count { (it * (raceTime - it)) > recordDistance }
        .toLong()
}

/**
 * @return The product of the number of winning moves
 */
fun part1(map: Map<Long, Long>): Long {
    return map
        .map { getPossibleWinCount(it.key, it.value) }
        .reduce { acc, i -> acc * i }
}

/**
 * @return The number of winning moves
 */
fun part2(map: Map<Long, Long>): Long {
    val time = map.keys.joinToString(separator = "") { it.toString() }.toLong()
    val dist = map.values.joinToString(separator = "") { it.toString() }.toLong()
    return getPossibleWinCount(time, dist)
}

fun day6() {
    val input = parseInput(DataFile.Part1)
    report(
        year = 2023,
        dayNumber = 6,
        part1 = part1(input),
        part2 = part2(input),
    )
}
