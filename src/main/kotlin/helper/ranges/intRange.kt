package helper.ranges

/**
 * Determine the overlap of two [IntRange], and return the result as an [IntRange]
 *
 * @param other The second range
 * @return The intersection of the two ranges, or an empty range if there is no overlap
 */
fun IntRange.intersect(other: IntRange): IntRange {
    val intersect = this.intersect(other)
    return if (intersect.isEmpty()) {
        IntRange.EMPTY
    } else {
        intersect.min()..intersect.max()
    }
}