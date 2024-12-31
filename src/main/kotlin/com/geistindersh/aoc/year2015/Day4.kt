package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import java.security.MessageDigest

class Day4(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val data = fileToString(2015, 4, dataFile)

    private fun hashWithData(i: Int) =
        MessageDigest
            .getInstance("MD5")
            .digest((data + i.toString()).toByteArray())
            .joinToString("") { "%02x".format(it) }

    override fun part1() = generateSequence(0) { it + 1 }.first { hashWithData(it).startsWith("00000") }

    override fun part2() = generateSequence(0) { it + 1 }.first { hashWithData(it).startsWith("000000") }
}

fun day4() {
    val day = Day4(DataFile.Part1)
    report(2015, 4, day.part1(), day.part2())
}
