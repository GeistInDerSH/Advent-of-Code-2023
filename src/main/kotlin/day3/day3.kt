package day3

import helper.fileToStream
import helper.report
import kotlin.math.max
import kotlin.math.min

data class Symbol(val name: Char, val row: Int, val col: Int) {
    private val touchingNumbers: MutableList<Int> = mutableListOf()

    fun addAnyTouchingNumbers(numbers: List<Number>) {
        for (number in numbers) {
            if (number.row !in (row - 1..row + 1))
                continue
            if (max(number.colStart, col - 1) > min(number.colEnd, col + 1))
                continue
            touchingNumbers.add(number.num)
        }
    }

    fun touchingNumbersCount() = touchingNumbers.size
    fun touchingNumbersProduct(): Int = touchingNumbers.fold(1) { acc, number -> acc * number }
}

data class Number(val num: Int, val row: Int, val colStart: Int, val colEnd: Int) {
    private var hasTouchingSymbol = false

    fun checkForTouchingSymbols(symbols: List<Symbol>) {
        if (hasTouchingSymbol)
            return
        for (symbol in symbols) {
            if (row !in (symbol.row - 1..symbol.row + 1))
                continue
            if (max(colStart, symbol.col - 1) > min(colEnd, symbol.col + 1))
                continue
            hasTouchingSymbol = true
            break
        }
    }

    fun hasTouchingSymbol() = hasTouchingSymbol
}

fun parseInput(fileName: String): Pair<List<Symbol>, List<Number>> {
    val lines = fileToStream(fileName).toList()

    val symbols = lines.mapIndexed { row, line ->
        line.mapIndexed { col, char ->
            if (char.isDigit() || char == '.') {
                null
            } else {
                Symbol(char, row, col)
            }
        }.filterNotNull()
    }.flatten().toList()

    val numbers = lines.mapIndexed { row, line ->
        val numbers: MutableList<Number> = mutableListOf()
        val num = StringBuilder()
        line.forEachIndexed { index, char ->
            if (char.isDigit()) {
                num.append(char)
            } else if (!char.isDigit() && num.isNotEmpty()) {
                numbers.add(Number(num.toString().toInt(), row, index - num.length, index - 1))
                num.clear()
            }
            if (index + 1 >= line.length && num.isNotEmpty()) {
                numbers.add(Number(num.toString().toInt(), row, index - num.length, line.length))
            }
        }

        numbers
    }.flatten().toList()

    return Pair(symbols, numbers)
}

fun part1(symbols: List<Symbol>, numbers: List<Number>): Int {
    return numbers.map { num ->
        num.checkForTouchingSymbols(symbols)
        num
    }.filter {
        it.hasTouchingSymbol()
    }.sumOf {
        it.num
    }
}

fun part2(symbols: List<Symbol>, numbers: List<Number>): Int {
    return symbols.map { sym ->
        sym.addAnyTouchingNumbers(numbers)
        sym
    }.filter {
        it.name == '*' && it.touchingNumbersCount() == 2
    }.sumOf {
        it.touchingNumbersProduct()
    }
}

fun day3() {
    val (symbols, numbers) = parseInput("src/main/resources/day_3/part_1.txt")
    report(
        dayNumber = 3,
        part1 = part1(symbols, numbers),
        part2 = part2(symbols, numbers),
    )
}