package com.geistindersh.aoc.helper.strings

import com.geistindersh.aoc.helper.enums.Point2D

/**
 * Remove all instances of the [chars] from [this]
 *
 * @param chars All chars to remove
 * @return String with the chars removed
 */
fun String.removeAll(chars: String): String {
    var result = this
    for (c in chars) {
        result = result.replace(c.toString(), "")
    }
    return result
}

/**
 * Convert a list of string into a hashmap of [Point2D] to [Char]
 * @return A map of [Point2D] to the [Char] at that point in the list of strings
 */
fun List<String>.toGrid2D() = this.flatMapIndexed { row, line -> line.mapIndexed { col, char -> Point2D(row, col) to char } }.toMap()

/**
 * Convert a list of string into a hashmap of [Point2D] to [Char]
 * @return A map of [Point2D] to the [Char] at that point in the list of strings
 */
fun <T> List<String>.toGrid2D(transform: (Char) -> T) =
    this
        .flatMapIndexed { row, line ->
            line.mapIndexed { col, char -> Point2D(row, col) to transform(char) }
        }.toMap()

/**
 * Convert a string to a grid of [Point2D] to [Char] by splitting at the [lineDelimiter]
 *
 * @param lineDelimiter The string to use to split the initial string
 * @return A grid of [Point2D] to the chars at the point
 */
fun String.toGrid2D(lineDelimiter: String = "\n") = this.split(lineDelimiter).toGrid2D()

/**
 * Convert a string to a grid of [Point2D] to [Char] by splitting at the [lineDelimiter]
 *
 * @param lineDelimiter The string to use to split the initial string
 * @return A grid of [Point2D] to the chars at the point
 */
fun <T> String.toGrid2D(
    lineDelimiter: String = "\n",
    transform: (Char) -> T,
) = this.split(lineDelimiter).toGrid2D(transform)

/**
 * Convert a list of string into a hashmap of [Point2D] to [Char] without null values
 * @return A map of [Point2D] to the [Char] at that point in the list of strings
 */
fun <T> List<String>.toGrid2DRemoveNull(transform: (Char) -> T?): Map<Point2D, T> =
    this
        .flatMapIndexed { row, line ->
            line
                .mapIndexedNotNull { col, char ->
                    val t = transform(char) ?: return@mapIndexedNotNull null
                    Point2D(row, col) to t
                }
        }.toMap()

/**
 * Convert a string to a grid of [Point2D] to [Char] by splitting at the [lineDelimiter]
 *
 * @param lineDelimiter The string to use to split the initial string
 * @return A grid of [Point2D] to the chars at the point
 */
fun <T> String.toGrid2DRemoveNull(
    lineDelimiter: String = "\n",
    transform: (Char) -> T?,
) = this.split(lineDelimiter).toGrid2DRemoveNull(transform)
