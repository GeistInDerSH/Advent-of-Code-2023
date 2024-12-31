package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day8(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val rowMajor =
        fileToStream(2022, 8, dataFile)
            .map { line ->
                line
                    .toCharArray()
                    .map { it.digitToInt() }
                    .toList()
            }.toList()
    private val columnMajor =
        rowMajor.indices
            .map { row ->
                rowMajor.indices.map { column -> rowMajor[column][row] }
            }.toList()

    private fun isVisible(
        row: Int,
        column: Int,
    ): Boolean {
        return when {
            row == 0 -> true
            column == 0 -> true
            row == columnMajor.size - 1 -> true
            column == rowMajor.size - 1 -> true
            else -> {
                val value = rowMajor[row][column]
                val leftVis = rowMajor[row].take(column).all { it < value }
                val rightVis = rowMajor[row].drop(column + 1).all { it < value }
                val topVis = columnMajor[column].take(row).all { it < value }
                val botVis = columnMajor[column].drop(row + 1).all { it < value }
                return leftVis || rightVis || topVis || botVis
            }
        }
    }

    private fun score(
        row: Int,
        column: Int,
    ): Int =
        when {
            row == 0 -> 0
            column == 0 -> 0
            row == columnMajor.size - 1 -> 1
            column == rowMajor.size - 1 -> 1
            else -> {
                val value = rowMajor[row][column]
                val leftScore =
                    rowMajor[row]
                        .take(column)
                        .reversed()
                        .takeWhileInclusive { it < value }
                        .count()
                val rightScore = rowMajor[row].drop(column + 1).takeWhileInclusive { it < value }.count()
                val topScore =
                    columnMajor[column]
                        .take(row)
                        .reversed()
                        .takeWhileInclusive { it < value }
                        .count()
                val botScore = columnMajor[column].drop(row + 1).takeWhileInclusive { it < value }.count()
                leftScore * rightScore * topScore * botScore
            }
        }

    override fun part1(): Int =
        rowMajor
            .indices
            .flatMap { row ->
                columnMajor.indices.map { isVisible(row, it) }
            }.count { it }

    override fun part2(): Int =
        rowMajor
            .indices
            .flatMap { row ->
                columnMajor.indices.map { score(row, it) }
            }.max()
}

fun day8() {
    val day = Day8(DataFile.Part1)
    report(2022, 8, day.part1(), day.part2())
}
