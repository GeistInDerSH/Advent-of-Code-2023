package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day13(
    dataFile: DataFile,
) : AoC<Long, Long> {
    private val numbers =
        fileToString(2020, 13, dataFile)
            .replace("\n", ",")
            .split(",")
            .map { it.toLongOrNull() }
            .toList()
    private val departTimestamp = numbers[0]!!
    private val times = numbers.drop(1)
    private val validTimes = times.filterNotNull()

    private data class Bus(
        val id: Long,
        val offset: Long,
    ) {
        constructor(id: Long, offset: Int) : this(id, offset.toLong())

        fun meetsSchedule(time: Long) = (time + offset) % id == 0L
    }

    override fun part1() =
        validTimes
            .map { it to it - (departTimestamp % it) }
            .minBy { it.second }
            .toList()
            .reduce(Long::times)

    override fun part2(): Long {
        val slowestBus =
            validTimes
                .maxBy { it }
                .let { Bus(it, times.indexOf(it)) }
        val secondSlowestBus =
            validTimes
                .filter { it != slowestBus.id }
                .maxBy { it }
                .let { Bus(it, times.indexOf(it)) }

        // Only need to check the second bus, because the first will always meet the schedule
        var timestamp = slowestBus.id - slowestBus.offset
        while (!secondSlowestBus.meetsSchedule(timestamp)) {
            timestamp += slowestBus.id
        }

        val remainingBuses =
            validTimes
                .map { Bus(it, times.indexOf(it)) }
                .filter { it != slowestBus || it != secondSlowestBus }
                .toList()

        val increment = slowestBus.id * secondSlowestBus.id
        while (!remainingBuses.all { it.meetsSchedule(timestamp) }) {
            timestamp += increment
        }

        return timestamp
    }
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2020, 13, day.part1(), day.part2())
}
