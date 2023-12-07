package day7

import helper.fileToStream
import helper.report

data class Hand(val cards: List<Char>, val bid: Long) {
    private val frequency = cards.associateWith { c -> cards.count { it == c } }
    fun kind(): Int {
        val size = frequency.keys.size
        return when {
            size == 1 -> 5
            size == 2 && 4 in frequency.values -> 4
            size == 2 && 3 in frequency.values -> 3
            size == 3 && 3 in frequency.values -> 2
            size == 3 && frequency.values.count { it == 2 } == 2 -> 1
            size == 4 -> 0
            else -> -1
        }
    }

    companion object : Comparator<Hand> {
        override fun compare(o1: Hand, o2: Hand): Int {
            val l = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
            return when {
                o1.kind() > o2.kind() -> 1
                o1.kind() < o2.kind() -> -1
                else -> {
                    for ((c1, c2) in o1.cards.zip(o2.cards)) {
                        if (c1 == c2) {
                            continue
                        }
                        val ci1 = l.indexOf(c1)
                        val ci2 = l.indexOf(c2)
                        if (ci1 > ci2) {
                            return -1
                        } else if (ci1 < ci2) {
                            return 1
                        }
                    }
                    0
                }
            }
        }
    }
}

fun parseInput(fileName: String): List<Hand> {
    return fileToStream(fileName).map {
        val (cards, bid) = it.split(' ')
        Hand(cards.toList(), bid.toLong())
    }.toList().sortedWith(Hand)
}

fun part1(hands: List<Hand>) = hands.mapIndexed { i, hand -> (i + 1) * hand.bid }.sum()

fun day7() {
    val input = parseInput("src/main/resources/day_7/part_1.txt")
    // val input = parseInput("src/main/resources/day_7/example.txt")
    report(
        dayNumber = 7,
        part1 = part1(input),
        part2 = "",
    )
}
