package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day21(dataFile: DataFile) {
	private val game = fileToStream(2021, 21, dataFile)
		.map { line ->
			val (index, start) = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
			Player(index, start, 0)
		}
		.toList()
		.sortedBy { it.index }
		.let { Game(it) }

	private data class Player(val index: Int, val position: Int, val score: Int) {
		fun next(rolls: List<Int>): Player {
			val position = (position + rolls.sum()) % 10
			val score = score + if (position == 0) 10 else position
			return this.copy(position = if (position == 0) 10 else position, score = score)
		}
	}

	private data class Game(val players: List<Player>, val dieState: Int = 1, val rollCount: Long = 0) {
		fun next(): Game {
			val updatedPlayers = mutableListOf<Player>()
			var state = dieState
			var count = 0L
			var foundWinner = false
			for (player in players) {
				if (foundWinner) {
					updatedPlayers.add(player)
					continue
				}

				val rolls = generateSequence(state) { ((it + 1) % 101).coerceAtLeast(1) }
					.take(3)
					.toList()
				count += 3
				state = ((rolls.last() + 1) % 101).coerceAtLeast(1)
				val nextPlayerState = player.next(rolls)
				updatedPlayers.add(nextPlayerState)
				foundWinner = nextPlayerState.score >= 1000
			}

			return Game(updatedPlayers, dieState = state, rollCount = count + rollCount)
		}

		fun winner() = players.firstOrNull { it.score >= 1000 }
	}

	private fun play() = generateSequence(game) { it.next() }
		.dropWhile { it.winner() == null }
		.take(1)
		.first()

	fun part1() = play().let {
		val loser = it.players.first { p -> p.score < 1000 }
		loser.score * it.rollCount
	}

	fun part2() = 0
}

fun day21() {
	val day = Day21(DataFile.Part1)
	report(2021, 21, day.part1(), day.part2())
}
