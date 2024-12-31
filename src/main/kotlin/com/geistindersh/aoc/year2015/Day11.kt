package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day11(
    dataFile: DataFile,
) : AoC<String, String> {
    private val data = fileToString(2015, 11, dataFile)

    private fun String.hasMonotonicAlphabeticSeq() =
        this
            .windowed(3) { seq ->
                val (a, b, c) = seq.map { it.code - 'a'.code }.toList()
                b == (a % ALPHABET.length) + 1 && c == (b % ALPHABET.length) + 1
            }.any { it }

    private fun String.containsBadChars() = this.toSet().any { it in setOf('i', 'o', 'l') }

    private fun String.containsUniqueLetterPairs() =
        this
            .windowed(2)
            .filter { it.first() == it.last() }
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= 1 }
            .count() >= 2

    private fun isInvalidPassword(str: String) = !isValidPassword(str)

    private fun isValidPassword(str: String) = str.hasMonotonicAlphabeticSeq() && !str.containsBadChars() && str.containsUniqueLetterPairs()

    private fun String.next(): String {
        val builder = StringBuilder()
        var carry = 1
        this.reversed().forEachIndexed { index, chr ->
            if (carry == 0) {
                builder.insert(0, chr)
            } else {
                val idx = ALPHABET.indexOf(chr) + 1
                val newChr = ALPHABET[idx % ALPHABET.length]
                if (newChr == 'a') {
                    if (index == this.lastIndex) {
                        builder.insert(0, 'a')
                    }
                    carry = 1
                } else {
                    carry = 0
                }
                builder.insert(0, newChr)
            }
        }
        return builder.toString()
    }

    private fun String.nextValid() =
        generateSequence(this.next()) { it.next() }
            .dropWhile(::isInvalidPassword)
            .first()

    override fun part1() = data.nextValid()

    override fun part2() = data.nextValid().nextValid()

    companion object {
        const val ALPHABET = "abcdefghijklmnopqrstuvwxyz"
    }
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2015, 11, day.part1(), day.part2())
}
