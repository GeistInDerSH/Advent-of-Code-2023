package com.geistindersh.aoc.helper.ranges

/**
 * Determine the overlap of two [IntRange], and return the result as an [IntRange]
 *
 * @param other The second range
 * @return The intersection of the two ranges, or an empty range if there is no overlap
 */
fun IntRange.intersect(other: IntRange): IntRange {
    val intersect = this.intersect(other.toSet())
    return if (intersect.isEmpty()) {
        IntRange.EMPTY
    } else {
        intersect.min()..intersect.max()
    }
}

/**
 * Check to see if the given coordinates overlap
 *
 * @param other The [IntRange] to check for an overlap in
 * @return If there is an overlap between the two ranges of numbers
 */
@Suppress("unused")
fun IntRange.hasOverlap(other: IntRange): Boolean = hasOverlap(this.first, other.last, this.last, other.first)

/**
 * Check to see if the given coordinates overlap
 *
 * @param start1 The start of the first range
 * @param end1 The end of the first range
 * @param start2 The start of the second range
 * @param end2 The end of the second range
 * @return If there is an overlap between the two ranges of numbers
 */
fun hasOverlap(
    start1: Int,
    end1: Int,
    start2: Int,
    end2: Int,
): Boolean = start1 <= end1 && end2 <= start2

fun IntRange.isFullyContained(other: IntRange): Boolean = this.toSet().containsAll(other.toSet())

/**
 * Generate a new range of two ranges that already have an overlap in their bounds.
 *
 * @param other The [IntRange] to generate the intersection with
 * @return A new range, that contains both ranges
 */
fun IntRange.intersection(other: IntRange): IntRange = maxOf(this.first, other.first)..minOf(this.last, other.last)

/**
 * Count the number of elements in the [IntRange]. It's faster than calling IntRange::count
 * because it doesn't need to loop over the whole range
 *
 * @return The size of the [IntRange]
 */
fun IntRange.size(): Int = this.last - this.first + 1
