package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day22(dataFile: DataFile) {
    private val players = fileToString(2020, 22, dataFile)
        .split("\n\n")
        .map { Player.from(it.split("\n")) }
        .toList()

    private data class Player(val id: Int, val deck: List<Int>) {
        val hasCards = deck.isNotEmpty()

        fun topCard() = deck.first()
        fun score() = deck
            .reversed()
            .withIndex()
            .fold(0L) { acc, card -> acc + card.value * (card.index + 1) }

        companion object {
            val NUMBER_REGEX = "[0-9]+".toRegex()

            fun from(lines: List<String>): Player {
                val id = NUMBER_REGEX.find(lines[0])!!.value.toInt()
                val deck = lines.drop(1).map { it.toInt() }
                return Player(id, deck)
            }
        }
    }

    private fun List<Player>.play(): Player {
        var p1 = this.first()
        var p2 = this.last()

        while (p1.hasCards && p2.hasCards) {
            val p1Top = p1.topCard()
            val p2Top = p2.topCard()
            if (p1Top > p2Top) {
                p1 = p1.copy(deck = p1.deck.drop(1) + p1Top + p2Top)
                p2 = p2.copy(deck = p2.deck.drop(1))
            } else {
                p1 = p1.copy(deck = p1.deck.drop(1))
                p2 = p2.copy(deck = p2.deck.drop(1) + p2Top + p1Top)
            }
        }

        return if (p1.hasCards) p1 else p2
    }

    fun part1() = players.play().score()
    fun part2() = 0
}

fun day22() {
    val day = Day22(DataFile.Part1)
    report(2020, 22, day.part1(), day.part2())
}
