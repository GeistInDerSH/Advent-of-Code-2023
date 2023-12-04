package day4

import helper.fileToStream
import helper.report
import kotlin.math.max
import kotlin.math.pow

data class Card(val number: Int, val winners: Set<Int>, val mine: Set<Int>) {
    fun matchCount() = winners.intersect(mine).size
    fun points() = max(2f.pow(matchCount() - 1), 0f).toInt()
}

fun parseInput(fileName: String): List<Card> {
    return fileToStream(fileName).mapIndexed { index, line ->
        val sections = line.substringAfter(':').split('|')
        val winners = sections[0].split(' ').filter { it.isNotBlank() }.map { it.toInt() }.toSet()
        val mine = sections[1].split(' ').filter { it.isNotBlank() }.map { it.toInt() }.toSet()
        Card(index + 1, winners, mine)
    }.toList()
}

fun part1(cards: List<Card>): Int = cards.sumOf { it.points() }

fun part2(cards: List<Card>): Int {
    val cardCounts = cards.associate { it.number to 1 }.toMutableMap()
    for (card in cards) {
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
    val cards = parseInput("src/main/resources/day_4/part_1.txt")
    report(
        dayNumber = 4,
        part1 = part1(cards),
        part2 = part2(cards)
    )
}