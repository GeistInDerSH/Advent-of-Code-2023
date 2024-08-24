package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import kotlin.math.absoluteValue

class Day9(dataFile: DataFile) {
    private val moves = fileToStream(2022, 9, dataFile)
        .flatMap { line ->
            val parts = line.split(" ")
            val move = parts[0][0]
            val dist = parts[1].toInt()
            (0..<dist).map { move }
        }.map {
            when (it) {
                'R' -> Pair(1, 0)
                'L' -> Pair(-1, 0)
                'U' -> Pair(0, 1)
                'D' -> Pair(0, -1)
                else -> throw IllegalArgumentException("$it")
            }
        }
        .toList()

    private fun areTouching(offset: Pair<Int, Int>) =
        offset.first.absoluteValue <= 1 && offset.second.absoluteValue <= 1

    private fun signOf(i: Int): Int {
        return when {
            i > 0 -> 1
            i == 0 -> 0
            else -> -1
        }
    }

    private fun moveCloser(offset: Pair<Int, Int>, tail: Pair<Int, Int>) =
        Pair(tail.first + signOf(offset.first), tail.second + signOf(offset.second))

    private fun movePair(
        direction: Pair<Int, Int>,
        head: Pair<Int, Int>,
        tail: Pair<Int, Int>
    ): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val newHead = Pair(head.first + direction.first, head.second + direction.second)
        val offset = Pair(newHead.first - tail.first, newHead.second - tail.second)
        val newTail = if (areTouching(offset)) {
            tail
        } else {
            moveCloser(offset, tail)
        }

        return Pair(newHead, newTail)
    }

    private fun calculateDirection(p1: Pair<Int, Int>, p2: Pair<Int, Int>) =
        Pair(p2.first - p1.first, p2.second - p1.second)

    private fun moveChain(direction: Pair<Int, Int>, chain: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val localChain = chain + listOf(Pair(0, 0))
        val newChain = mutableListOf<Pair<Int, Int>>()
        var dir = direction

        localChain.windowed(2) { (head, tail) ->
            val (newHead, newTail) = movePair(dir, head, tail)
            newChain.add(newHead)
            dir = calculateDirection(tail, newTail)
        }

        return newChain
    }

    private fun trackTailOfChain(tailLength: Int): Set<Pair<Int, Int>> {
        val tails = mutableSetOf(Pair(0, 0))
        var chain = (0..<tailLength).map { Pair(0, 0) }

        for (move in moves) {
            chain = moveChain(move, chain)
            tails.add(chain.last())
        }

        return tails
    }

    fun part1() = trackTailOfChain(2).count()
    fun part2() = trackTailOfChain(10).count()
}

fun day9() {

}