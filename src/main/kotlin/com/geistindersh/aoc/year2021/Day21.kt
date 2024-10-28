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
		.let { (p1, p2) -> Game(p1, p2) }

	private data class Player(val index: Int, val position: Int, val score: Int) {
		fun next(moves: Int): Player {
			val position = (position + moves - 1) % 10 + 1
			return this.copy(position = position, score = score + position)
		}
	}

	private data class Game(
		val player1: Player,
		val player2: Player,
		val scoreLimit: Int = 1000,
		val isPlayer1Turn: Boolean = true,
	) {
		fun next(moves: Int) = this.copy(
			player1 = if (isPlayer1Turn) player1.next(moves) else player1,
			player2 = if (!isPlayer1Turn) player2.next(moves) else player2,
			isPlayer1Turn = !isPlayer1Turn
		)

		fun winner() = when {
			player1.score >= scoreLimit -> player1
			player2.score >= scoreLimit -> player2
			else -> null
		}
	}

	private class Die(private var rolls: Int = 0, private var value: Int = 0) {
		fun totalRolls() = rolls
		fun next(): Int {
			rolls += 3
			value += 3
			return 3 * value - 3
		}
	}

	fun part1(): Int {
		val die = Die()
		val end = generateSequence(game) { it.next(die.next()) }
			.dropWhile { it.winner() == null }
			.take(1)
			.first()
		val loser = if (end.player1.score >= end.scoreLimit) end.player2 else end.player1
		return loser.score * die.totalRolls()
	}

	fun part2(): Long {
		val frequency = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
		val history = mutableMapOf<Game, Pair<Long, Long>>()

		fun play(game: Game): Pair<Long, Long> {
			val winner = game.winner()
			if (winner != null) return if (winner.index == 1) Pair(1, 0) else Pair(0, 1)
			if (game in history) return history[game]!!
			return frequency
				.map { (die, freq) ->
					val (winsP1, winsP2) = play(game.next(die))
					Pair(winsP1 * freq, winsP2 * freq)
				}
				.reduce { (wins1, wins2), (score1, score2) -> (wins1 + score1) to (wins2 + score2) }
				.also { history[game] = it }
		}

		return play(game.copy(scoreLimit = 21)).toList().maxOf { it }
	}
}

fun day21() {
	val day = Day21(DataFile.Part1)
	report(2021, 21, day.part1(), day.part2())
}
