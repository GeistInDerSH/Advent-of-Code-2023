package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

enum class RPS(private val value: Int) {
    Rock(1),
    Paper(2),
    Scissors(3);

    fun getScores(other: RPS): Pair<Int, Int> {
        val (thisScore, otherScore) = when {
            this == other -> Pair(3 + this.value, 3)
            this == Rock && other == Scissors -> Pair(6, 0)
            this == Paper && other == Rock -> Pair(6, 0)
            this == Scissors && other == Paper -> Pair(6, 0)
            else -> Pair(0, 6)
        }
        return Pair(thisScore + value, otherScore + other.value)
    }

    fun getScores(other: String) = getScores(fromString(other))

    fun getWinningMove(): RPS {
        return when (this) {
            Rock -> Paper
            Paper -> Scissors
            Scissors -> Rock
        }
    }

    fun getLosingMove(): RPS {
        return when (this) {
            Paper -> Rock
            Scissors -> Paper
            Rock -> Scissors
        }
    }

    companion object {
        fun fromString(string: String): RPS {
            return when (string) {
                "A", "X" -> Rock
                "B", "Y" -> Paper
                "C", "Z" -> Scissors
                else -> throw IllegalArgumentException("$string is not a valid RPS argument")
            }
        }
    }
}

class Day2(dataFile: DataFile) {
    private val plays = fileToStream(2022, 2, dataFile)
        .map {
            val parts = it.split(" ")
            val play = RPS.fromString(parts[0])
            Pair(play, parts[1])
        }.toList()

    fun part1(): Int {
        return plays
            .map { (p1, p2) -> p1.getScores(p2).second }
            .fold(0, Int::plus)
    }

    fun part2(): Int {
        return plays.map { (p1, p2) ->
            val ourMove = when (p2) {
                "X" -> p1.getLosingMove()
                "Y" -> p1
                "Z" -> p1.getWinningMove()
                else -> throw IllegalArgumentException("$p2 is not a valid RPS argument")
            }
            Pair(p1, ourMove)
        }
            .map { (p1, p2) -> p1.getScores(p2).second }
            .fold(0, Int::plus)
    }
}

fun day2() {
    val day = Day2(DataFile.Part1)
    report(2022, 2, day.part1(), day.part2())
}