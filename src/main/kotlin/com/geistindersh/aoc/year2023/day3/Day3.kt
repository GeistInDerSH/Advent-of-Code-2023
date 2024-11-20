package com.geistindersh.aoc.year2023.day3

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.max
import kotlin.math.min

data class Symbol(
    val name: Char,
    val row: Int,
    val col: Int,
) {
    private val touchingNumbers: MutableSet<Int> = mutableSetOf()

    /**
     * Check for any [Number] that is touching the current [Symbol], and add it to known touching numbers
     *
     * @param numbers A list of Number that may be touching the current symbol
     */
    fun addAnyTouchingNumbers(numbers: List<Number>) {
        val toAdd = numbers.filter { hasOverlap(it.row, row, it.colStart, it.colEnd, col) }.map { it.num }.toSet()
        touchingNumbers.addAll(toAdd)
    }

    /**
     * @return The number of touching [Number]s
     */
    fun touchingNumbersCount() = touchingNumbers.size

    /**
     * @return The product of the touching [Number]s
     */
    fun touchingNumbersProduct(): Int = touchingNumbers.fold(1) { acc, number -> acc * number }
}

data class Number(
    val num: Int,
    val row: Int,
    val colStart: Int,
    val colEnd: Int,
) {
    private var hasTouchingSymbol = false

    /**
     * Check each of the [symbols] to see if any of them touch
     *
     * @param symbols A list of [Symbol] to check if the current [Number] touches
     */
    fun checkForTouchingSymbols(symbols: List<Symbol>) {
        if (hasTouchingSymbol) {
            return
        }
        hasTouchingSymbol = symbols.any { hasOverlap(it.row, row, colStart, colEnd, it.col) }
    }

    fun hasTouchingSymbol() = hasTouchingSymbol
}

/**
 * Check to see if there is an overlap between both the [srcRow] and the bounds of the [destRow], as well as an overlap
 * between the source and destination column bounds
 *
 * @param srcRow The source row index
 * @param destRow The destination row index
 * @param srcColStart The source column starting index
 * @param srcColEnd The source column ending index
 * @param destCol The destination column index
 */
fun hasOverlap(
    srcRow: Int,
    destRow: Int,
    srcColStart: Int,
    srcColEnd: Int,
    destCol: Int,
): Boolean = srcRow in (destRow - 1..destRow + 1) && max(srcColStart, destCol - 1) <= min(srcColEnd, destCol + 1)

fun parseInput(fileType: DataFile): Pair<List<Symbol>, List<Number>> {
    val lines = fileToStream(2023, 3, fileType).toList()

    // Extract only the symbols; these are any special characters that are non-`.`
    val symbols =
        lines.flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, char ->
                if (char.isDigit() || char == '.') {
                    null
                } else {
                    Symbol(char, row, col)
                }
            }
        }

    // Extract the numbers, and ensure that "1234" is 1234 not 1,2,3,4
    val numbers =
        lines.flatMapIndexed { row, line ->
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
        }

    return Pair(symbols, numbers)
}

/**
 * Sum up the value of the [Number]s with touching [Symbol]s
 *
 * @param symbols The list of parsed Symbols to use
 * @param numbers The list of parsed Numbers to use
 * @return The sum of numbers with touching symbols
 */
fun part1(
    symbols: List<Symbol>,
    numbers: List<Number>,
): Int =
    numbers
        .map { num ->
            num.checkForTouchingSymbols(symbols)
            num
        }.filter { it.hasTouchingSymbol() }
        .sumOf { it.num }

/**
 * Sum up the product of each [Symbol] named "*" with exactly 2 touching numbers
 *
 * @param symbols The list of parsed Symbols to use
 * @param numbers The list of parsed Numbers to use
 * @return The sum of the product of '*' symbols with 2 touching numbers
 */
fun part2(
    symbols: List<Symbol>,
    numbers: List<Number>,
): Int =
    symbols
        .map { sym ->
            sym.addAnyTouchingNumbers(numbers)
            sym
        }.filter { it.name == '*' && it.touchingNumbersCount() == 2 }
        .sumOf { it.touchingNumbersProduct() }

fun day3() {
    val (symbols, numbers) = parseInput(DataFile.Part1)
    report(
        year = 2023,
        dayNumber = 3,
        part1 = part1(symbols, numbers),
        part2 = part2(symbols, numbers),
    )
}
