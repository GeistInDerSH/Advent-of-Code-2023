package day7

import helper.fileToStream
import helper.report

data class Hand(val cards: List<Char>, val bid: Long, val includeJokers: Boolean) {
    // map of the characters to the number of instances of that char
    private val frequency = cards.associateWith { c -> cards.count { it == c } }

    // The frequency map with the jokers being replaced with the most frequent card
    private val frequencyNoJoker: Map<Char, Int> by lazy {
        if ('J' in frequency.keys) {
            val freq = frequency.toMutableMap()
            val jokerCount = freq.remove('J')!!
            val maxKey = freq.keys.sortedBy { freq[it] }.last()
            freq[maxKey] = freq[maxKey]!! + jokerCount
            freq.toMap()
        } else {
            frequency
        }
    }

    // Determine what kind of hand we have, this should be cached, so we don't re-calculate it when sorting
    // but can do so lazily in case we don't end up needing it
    private val kind: Int by lazy {
        if (frequency.keys.size == 1) {
            5
        } else {
            val frequency = if (includeJokers) frequencyNoJoker else frequency
            val size = frequency.keys.size
            when {
                size == 1 -> 5
                size == 2 && 4 in frequency.values -> 4
                size == 2 && 3 in frequency.values -> 3
                size == 3 && 3 in frequency.values -> 2
                size == 3 && frequency.values.count { it == 2 } == 2 -> 1
                size == 4 -> 0
                else -> -1
            }
        }
    }

    companion object : Comparator<Hand> {
        // The card ranking changes slightly between the two parts, and this should reflect that
        private val withJokers = "AKQT98765432J".toList()
        private val withoutJokers = "AKQJT98765432".toList()
        override fun compare(o1: Hand, o2: Hand): Int {
            val cards = if (o1.includeJokers) withJokers else withoutJokers
            return when {
                o1.kind > o2.kind -> 1
                o1.kind < o2.kind -> -1
                else -> {
                    // Go through each card and find the first that doesn't match and sort by that
                    for (i in 0..<o1.cards.size) {
                        val c1 = o1.cards[i]
                        val c2 = o2.cards[i]
                        if (c1 == c2) {
                            continue
                        }
                        val ci1 = cards.indexOf(c1)
                        val ci2 = cards.indexOf(c2)
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

fun parseInput(fileName: String, includeJokers: Boolean): List<Hand> {
    return fileToStream(fileName).map {
        val (cards, bid) = it.split(' ')
        Hand(cards.toList(), bid.toLong(), includeJokers)
    }.toList().sortedWith(Hand)
}

fun part1(fileName: String) = part1(parseInput(fileName, false))
fun part1(hands: List<Hand>) = hands.mapIndexed { i, hand -> (i + 1) * hand.bid }.sum()

fun part2(fileName: String) = part1(parseInput(fileName, true))

fun day7() {
    val fileName = "src/main/resources/day_7/part_1.txt"
    report(
        dayNumber = 7,
        part1 = part1(fileName),
        part2 = part2(fileName),
    )
}
