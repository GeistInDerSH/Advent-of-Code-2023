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
            val next = (current - 1).let {
                var next = it
                while (true) {
                    if (next < minCup) next = maxCup
                    if (next !in holding) break
                    next -= 1
                }
                next
            }

            val newList = mutableListOf(current).let {
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

        companion object {
            fun from(string: String) = Cups(string.map { it.digitToInt() })
        }
    }

    fun part1() = generateSequence(cups) { it.next() }
        .take(101)
        .last()
        .score()

    fun part2() = 0
}

fun day23() {
    val day = Day23(DataFile.Part1)
    report(2020, 23, day.part1(), day.part2())
}
