package helper.ranges

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
fun IntRange.hasOverlap(other: IntRange): Boolean {
    return hasOverlap(this.first, this.last, other.first, other.last)
}

/**
 * Check to see if the given coordinates overlap
 *
 * @param start1 The start of the first range
 * @param end1 The end of the first range
 * @param start2 The start of the second range
 * @param end2 The end of the second range
 * @return If there is an overlap between the two ranges of numbers
 */
fun hasOverlap(start1: Int, end1: Int, start2: Int, end2: Int): Boolean {
    return start1 <= end1 && end2 <= start2
}
