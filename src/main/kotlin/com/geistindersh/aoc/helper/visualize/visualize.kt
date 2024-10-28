package com.geistindersh.aoc.helper.visualize

import com.geistindersh.aoc.helper.enums.Point

/**
 * A [Map] extension function to help make it easier to visualize what the map
 * has in it, often because it is being used to store data for an image
 *
 * @param fn A function that takes a point, and returns a printable character
 */
fun <T> Map<Point, T>.print(fn: (Point) -> Char) {
	val data = this@print.keys.toList().sortedWith(compareBy<Point> { it.row }.thenBy { it.col })
	var row = data[0].row
	for (point in data) {
		if (row != point.row) {
			println()
			row = point.row
		}
		print(fn(point))
	}
	println()
}