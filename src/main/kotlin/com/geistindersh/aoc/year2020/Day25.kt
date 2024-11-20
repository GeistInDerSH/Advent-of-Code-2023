package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day25(
    dataFile: DataFile,
) {
    private val publicKeys = fileToStream(2020, 25, dataFile).map { it.toLong() }.toList()
    private val card = publicKeys.first()
    private val door = publicKeys.last()

    private fun Long.transform(value: Long) = (this * value) % 20201227

    private fun Long.getLoopSize() = generateSequence(1L) { it.transform(7) }.indexOf(this)

    private fun Long.getEncryptionKey(loopSize: Int) =
        generateSequence(1L) { it.transform(this) }
            .drop(loopSize)
            .first()

    fun part1() = card.getEncryptionKey(door.getLoopSize())

    fun part2() = "Pay the Deposit!" // Man, this one feels much less exciting than the others...
}

fun day25() {
    val day = Day25(DataFile.Part1)
    report(2020, 25, day.part1(), day.part2())
}
