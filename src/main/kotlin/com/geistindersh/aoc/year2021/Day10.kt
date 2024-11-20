package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*

class Day10(dataFile: DataFile) {
    private val chunks =
        fileToStream(2021, 10, dataFile)
            .map { it.toCharArray() }
            .toList()
    private val errorScoreTable =
        mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137,
        )
    private val autocompleteScoreTable =
        mapOf(
            ')' to 1L,
            ']' to 2L,
            '}' to 3L,
            '>' to 4L,
        )

    private fun findError(chunk: CharArray): Char? {
        val stack = Stack<Char>()
        for (char in chunk) {
            when (char) {
                '(', '[', '{', '<' -> stack.push(char)
                ')' -> {
                    val top = stack.pop()
                    if (top != '(') return char
                }

                ']' -> {
                    val top = stack.pop()
                    if (top != '[') return char
                }

                '}' -> {
                    val top = stack.pop()
                    if (top != '{') return char
                }

                '>' -> {
                    val top = stack.pop()
                    if (top != '<') return char
                }
            }
        }
        return null
    }

    private fun completeSequence(chunk: CharArray): String? {
        val stack = Stack<Char>()
        for (char in chunk) {
            when (char) {
                '(', '[', '{', '<' -> stack.push(char)
                ')' -> {
                    val top = stack.pop()
                    if (top != '(') return null
                }

                ']' -> {
                    val top = stack.pop()
                    if (top != '[') return null
                }

                '}' -> {
                    val top = stack.pop()
                    if (top != '{') return null
                }

                '>' -> {
                    val top = stack.pop()
                    if (top != '<') return null
                }
            }
        }

        val sb = StringBuilder()
        while (stack.isNotEmpty()) {
            val char =
                when (val top = stack.pop()) {
                    '(' -> ')'
                    '[' -> ']'
                    '{' -> '}'
                    '<' -> '>'
                    else -> throw IllegalStateException("$top")
                }
            sb.append(char)
        }

        return sb.toString()
    }

    fun part1() =
        chunks
            .mapNotNull(::findError)
            .sumOf { errorScoreTable[it]!! }

    fun part2() =
        chunks
            .mapNotNull(::completeSequence)
            .map { seq -> seq.map { autocompleteScoreTable[it]!! }.reduce { acc, s -> acc * 5 + s } }
            .sorted()
            .let { it[it.size / 2] }
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2021, 10, day.part1(), day.part2())
}
