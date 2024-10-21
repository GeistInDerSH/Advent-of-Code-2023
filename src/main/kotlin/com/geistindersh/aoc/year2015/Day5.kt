package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day5(dataFile: DataFile) {
    private val data = fileToStream(2015, 5, dataFile).toList()
    private val vowels = setOf('a', 'e', 'i', 'o', 'u')
    private val notAllowedSubstrings = setOf("ab", "cd", "pq", "xy")

    private fun countVowels(str: String) = str.count { vowels.contains(it) }
    private fun containsBadSubstring(str: String) = notAllowedSubstrings.any { str.contains(it) }
    private fun containsDoubleLetter(str: String) = str.windowed(2).any { it.first() == it.last() }

    private fun isNice(str: String) = countVowels(str) >= 3 && !containsBadSubstring(str) && containsDoubleLetter(str)

    fun part1() = data.count(::isNice)
    fun part2() = 0
}

fun day5() {
    val day = Day5(DataFile.Part1)
    report(2015, 5, day.part1(), day.part2())
}
