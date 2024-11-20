package com.geistindersh.aoc.year2023.day4

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.max
import kotlin.math.pow

data class Card(val number: Int, val winners: Set<Int>, val mine: Set<Int>) {
    /**
     * @return The number of matches the winning card has with ours
     */
    fun matchCount() = winners.intersect(mine).size

    /**
     * @return The number of points the card is worth
     */
    fun points() = max(2f.pow(matchCount() - 1), 0f).toInt()
}

fun parseInput(fileType: DataFile): List<Card> {
    return fileToStream(2023, 4, fileType).mapIndexed { index, line ->
        val (winners, mine) =
            line
                .substringAfter(':')
                .split('|')
                .map { section ->
                    section
                        .split(' ')
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }
                        .toSet()
                }

        Card(index + 1, winners, mine)
    }.toList()
}

/**
 * Sum the number of points each card is worth
 *
 * @param cards The cards dealt that we can play with
 * @return The sum of the points for each winning card
 */
fun part1(cards: List<Card>): Int = cards.sumOf { it.points() }

/**
 * Sum up the number of points for each card, when accounting for duplicates
 *
 * @param cards The cards dealt that we can play with
 * @return The sum of the points for each winning card
 */
fun part2(cards: List<Card>): Int {
    val cardCounts = cards.associate { it.number to 1 }.toMutableMap()
    for (card in cards) {
        // Walk down the remaining cards, and increment their winnings (so long as the bounds exist)
        for (i in 1..card.matchCount()) {
            val cardNum = card.number + i
            if (cardNum > cards.size) {
                break
            }
            cardCounts[cardNum] = cardCounts[cardNum]!! + cardCounts[card.number]!!
        }
    }
    return cardCounts.values.sumOf { it }
}

fun day4() {
    val cards = parseInput(DataFile.Part1)
    report(
        year = 2023,
        dayNumber = 4,
        part1 = part1(cards),
        part2 = part2(cards),
    )
}
