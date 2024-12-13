package com.geistindersh.aoc.helper.math

/**
 * Calculate the determinant of a 2x2 matrix.
 *
 * Reference: [https://en.wikipedia.org/wiki/Determinant](https://en.wikipedia.org/wiki/Determinant)
 *
 * @return A scalar value for the determinant
 */
fun determinant(
    a: Long,
    b: Long,
    c: Long,
    d: Long,
) = (a * d) - (b * c)
