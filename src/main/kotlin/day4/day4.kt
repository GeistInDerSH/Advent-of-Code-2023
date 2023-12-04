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
        println(sections)
        val winners = sections[0].trim().split(' ').filter { it.isNotBlank() }.map { it.toInt() }.toSet()
        val mine = sections[1].trim().split(' ').filter { it.isNotBlank() }.map { it.toInt() }.toSet()
        Card(index + 1, winners, mine)
    }.toList()
}

fun part1(cards: List<Card>): Int = cards.sumOf { it.points() }

fun part2(cards: List<Card>): Int {
    val c = cards.map { it.number }.associateWith { 1 }.toMutableMap()
    for (card in cards) {
        val count = card.matchCount()
        for (i in 1..count) {
            if (card.number + i > cards.size) {
                break
            }
            c[card.number + i] = c[card.number + i]!! + c[card.number]!!
        }
    }
    return c.values.sumOf { it }
}

fun day4() {
    val cards = parseInput("src/main/resources/day_4/part_1.txt")
    report(
        dayNumber = 4,
        part1 = part1(cards),
        part2 = part2(cards)
    )
}