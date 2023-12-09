package day9

import helper.fileToStream
import helper.report

data class Sensor(val history: MutableList<Int>) {
    private val extrapolated: MutableList<MutableList<Int>> = run {
        val list: MutableList<MutableList<Int>> = mutableListOf()
        var currentList = history.toList()
        while (true) {
            val newList: MutableList<Int> = mutableListOf()
            for (win in currentList.windowed(2)) {
                newList.add(win.last() - win.first())
            }
            if (newList.all { it == 0 }) {
                break
            }
            list.add(newList)
            currentList = newList
        }
        list
    }

    fun next(): Int {
        for (row in extrapolated.indices.reversed()) {
            if (row == extrapolated.size - 1) {
                extrapolated[row].add(extrapolated[row].last())
                continue
            }

            val current = extrapolated[row].last()
            val prev = extrapolated[row + 1].last()
            extrapolated[row].add(current + prev)
        }
        val hist = extrapolated.first().last()
        val toAdd = hist + history.last()
        history.add(toAdd)
        return toAdd
    }

    fun previous(): Int {
        for (row in extrapolated.indices.reversed()) {
            if (row == extrapolated.size - 1) {
                extrapolated[row].add(extrapolated[row].first())
                continue
            }

            val current = extrapolated[row].first()
            val prev = extrapolated[row + 1].first()
            extrapolated[row].addFirst(current - prev)
        }
        val hist = extrapolated.first().first()
        val toAdd = history.first() - hist
        history.addFirst(toAdd)
        return toAdd
    }
}

fun parseInput(fileName: String): List<Sensor> {
    return fileToStream(fileName).map { line ->
        Sensor(line.split(' ').map { it.toInt() }.toMutableList())
    }.toList()
}

fun part1(sequences: List<Sensor>) = sequences.sumOf { it.next() }

fun part2(sequences: List<Sensor>) = sequences.sumOf { it.previous() }

fun day9() {
    val input = parseInput("src/main/resources/day_9/part_1.txt")
    report(
        dayNumber = 9,
        part1 = part1(input),
        part2 = part2(input),
    )
}
