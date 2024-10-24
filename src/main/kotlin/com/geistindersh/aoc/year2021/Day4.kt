package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day4(dataFile: DataFile) {
	private val numbers = fileToStream(2021, 4, dataFile).first().split(",").map(String::toInt)
	private val boards = fileToString(2021, 4, dataFile)
		.split("\n\n")
		.drop(1)
		.map { chunk ->
			val re = "[0-9]+".toRegex()
			val values = chunk.split("\n").map { line -> re.findAll(line).map { it.value.toInt() }.toList() }
			BingoBoard(values)
		}

	private data class BingoBoard(val board: List<List<Int>>) {
		private val seen = Array(board.size) { BooleanArray(board[0].size) { false } }
		private var lastCalled = -1

		fun update(call: Int) {
			lastCalled = call
			for (row in board.indices) {
				for (col in board[0].indices) {
					if (board[row][col] == call) {
						seen[row][col] = true
					}
				}
			}
		}

		private fun hasRowBingo() = seen.any { row -> row.all { it } }
		private fun hasColBingo() = seen[0].indices.any { col -> seen.all { it[col] } }

		fun hasBingo() = hasRowBingo() || hasColBingo()

		fun score() = lastCalled * board
			.flatMapIndexed { row, ints ->
				ints.mapIndexed { col, i -> if (seen[row][col]) 0 else i }
			}
			.reduce(Int::plus)
	}

	fun part1(): Int {
		for (number in numbers) {
			for (board in boards) {
				board.update(number)
				if (board.hasBingo()) return board.score()
			}
		}
		return 0
	}

	fun part2(): Int {
		val boards = boards.toMutableList()
		for (number in numbers) {
			val toRemove = mutableListOf<BingoBoard>()
			for (board in boards) {
				board.update(number)
				if (board.hasBingo()) toRemove.add(board)
			}

			if (boards.size - toRemove.size == 0) return toRemove.last().score()
			boards.removeAll(toRemove)
		}
		return 0
	}
}

fun day4() {
	report(2021, 4, Day4(DataFile.Part1).part1(), Day4(DataFile.Part1).part2())
}
