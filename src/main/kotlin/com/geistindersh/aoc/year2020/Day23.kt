package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.rotateLeft
import com.geistindersh.aoc.helper.report

class Day23(dataFile: DataFile) {
    private val cups = Cups.from(fileToString(2020, 23, dataFile))

    private data class Cups(val numbers: List<Int>) {
        private val minCup = numbers.minOf { it }
        private val maxCup = numbers.maxOf { it }

        fun next(): Cups {
            val current = numbers.first()
            val holding = numbers.drop(1).take(3)
            val next =
                (current - 1).let {
                    var next = it
                    while (true) {
                        if (next < minCup) next = maxCup
                        if (next !in holding) break
                        next -= 1
                    }
                    next
                }

            val newList =
                mutableListOf(current).let {
                    val newList = it
                    var i = 4
                    while (numbers[i] != next) { // Add all cards up to next
                        newList.add(numbers[i])
                        i += 1
                    }
                    newList.add(next) // add next
                    newList.addAll(holding) // add what we are holding
                    newList.addAll(numbers.subList(i + 1, numbers.size)) // Add any remaining cards

                    newList.rotateLeft(1) // Rotate left so the next is always at the start
                }
            return Cups(newList)
        }

        fun score(): Long {
            val oneIndex = numbers.indexOf(1)
            val before = numbers.subList(0, oneIndex)
            val after = numbers.subList(oneIndex + 1, numbers.size)
            return (after + before).fold(0L) { acc, value -> (acc * 10) + value }
        }

        fun makePart2(): Cups {
            val max = numbers.maxOf { it }
            val toAdd = (max + 1..1_000_000)
            val newNums = numbers.toMutableList()
            newNums.addAll(toAdd)
            return Cups(newNums)
        }

        companion object {
            fun from(string: String) = Cups(string.map { it.digitToInt() })
        }
    }

    private class Cup(val value: Int) {
        lateinit var next: Cup

        fun nextCount(n: Int) =
            (1..n)
                .runningFold(this) { cur, _ -> cur.next }
                .drop(1)
    }

    fun part1() =
        generateSequence(cups) { it.next() }
            .take(101)
            .last()
            .score()

    fun part2(): Long {
        val allCups = List(1_000_001) { Cup(it) }

        cups.makePart2()
            .numbers
            .map { allCups[it] }
            .fold(allCups[cups.numbers.last()]) { prev, curr ->
                curr.also { prev.next = curr }
            }
        allCups.last().next = allCups[cups.numbers.first()]

        var current = allCups[cups.numbers.first()]
        repeat(10_000_000) {
            val holding = current.nextCount(3)

            // Find where to place the cups being held
            val destination =
                (current.value - 1).let { start ->
                    val holdValue = holding.map { it.value }.toSet()
                    var dest = start
                    while (dest in holdValue || dest == 0) {
                        dest = if (dest == 0) allCups.size - 1 else dest - 1
                    }
                    allCups[dest]
                }

            // Move the cups being held into position
            val prev = destination.next
            current.next = holding.last().next
            destination.next = holding.first()
            holding.last().next = prev

            current = current.next
        }

        return allCups[1]
            .nextCount(2)
            .map { it.value.toLong() }
            .reduce(Long::times)
    }
}

fun day23() {
    val day = Day23(DataFile.Part1)
    report(2020, 23, day.part1(), day.part2())
}
