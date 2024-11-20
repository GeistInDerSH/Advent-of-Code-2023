package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day2(
    dataFile: DataFile,
) {
    private val policies =
        fileToStream(2020, 2, dataFile)
            .map { PasswordPolicies.from(it) }
            .toList()

    private data class PasswordPolicies(
        val min: Int,
        val max: Int,
        val char: Char,
        val password: String,
    ) {
        fun hasCharCountInRange() = password.count { it == char } in min..max

        fun hasCharAtMinMaxPosition() = (password[min - 1] == char) xor (password[max - 1] == char)

        companion object {
            fun from(str: String): PasswordPolicies {
                val parts = str.replace(":", "").replace("-", " ").split(" ")
                return PasswordPolicies(parts[0].toInt(), parts[1].toInt(), parts[2][0], parts[3])
            }
        }
    }

    fun part1() = policies.count { it.hasCharCountInRange() }

    fun part2() = policies.count { it.hasCharAtMinMaxPosition() }
}

fun day2() {
    val day = Day2(DataFile.Part1)
    report(2020, 2, day.part1(), day.part2())
}
