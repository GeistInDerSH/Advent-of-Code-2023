package day6

import helper.fileToStream
import helper.report

fun parseInput(fileName: String): Map<Long, Long> {
    val lines = fileToStream(fileName).toList()
    val times = lines[0].substringAfter(':').trim().split(' ').filter { it.isNotBlank() }.map { it.toLong() }
    val distance = lines[1].substringAfter(':').trim().split(' ').filter { it.isNotBlank() }.map { it.toLong() }
    return times.zip(distance).associate { it.first to it.second }
}

/**
 * Calculate the number of possible winning moves that beat the [recordDistance] within the [raceTime]
 *
 * @param raceTime The time in ms for how long the race will go
 * @param recordDistance The distance to beat
 * @return The number of winning plays to make
 */
fun getPossibleWinCount(raceTime: Long, recordDistance: Long) =
    (0..raceTime).count { (it * (raceTime - it)) > recordDistance }.toLong()

/**
 * @return The product of the number of winning moves
 */
fun part1(map: Map<Long, Long>) = map.map { getPossibleWinCount(it.key, it.value) }.reduce { acc, i -> acc * i }

/**
 * @return The number of winning moves
 */
fun part2(map: Map<Long, Long>): Long {
    val time = map.keys.joinToString(separator = "") { it.toString() }.toLong()
    val dist = map.values.joinToString(separator = "") { it.toString() }.toLong()
    return getPossibleWinCount(time, dist)
}

fun day6() {
    val input = parseInput("src/main/resources/day_6/part_1.txt")
    report(
        dayNumber = 6,
        part1 = part1(input),
        part2 = part2(input),
    )
}
