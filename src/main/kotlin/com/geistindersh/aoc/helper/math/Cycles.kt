package com.geistindersh.aoc.helper.math

import kotlin.math.max

/**
 * @param a The first value
 * @param b The second value
 * @return The least common multiple of [a] and [b]
 */
fun lcm(
    a: Long,
    b: Long,
): Long {
    val larger = max(a, b)
    val maxLCM = a * b
    var lcm = larger
    while (lcm <= maxLCM) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLCM
}

/**
 * Determine the least common multiple of all values in the [Collection]
 *
 * @return The least common multiple
 */
fun Collection<Long>.lcm(): Long = this.fold(1L) { acc, i -> lcm(acc, i) }

tailrec fun gcd(
    a: Long,
    b: Long,
): Long = if (a == 0L) b else gcd(b % a, a)

tailrec fun gcd(
    a: Int,
    b: Int,
): Int = if (a == 0) b else gcd(b % a, a)
