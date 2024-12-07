package com.geistindersh.aoc.helper.binary

import kotlin.math.absoluteValue
import kotlin.math.log10

/**
 * Set the bit at the given [position] to the [value]
 *
 * @param position The position to set the bit at
 * @param value The value to set the bit to (0 or 1)
 * @return A copy of [this] with the updated bit
 */
fun Int.setBit(
    position: Int,
    value: Int,
): Int = this and (1 shl position).inv() or (value shl position)

/**
 * Get the value of the bit at the [position]
 *
 * @param position The position to get the bit value
 * @return 0 or 1
 */
fun Int.bitAt(position: Int): Int = (this shr position) and 1

/**
 * Count the number of bits in the [Int]
 *
 * @return The number of bits
 */
fun Int.bitCount() = this.toLong().bitCount()

/**
 * Set the bit at the given [position] to the [value]
 *
 * @param position The position to set the bit at
 * @param value The value to set the bit to (0 or 1)
 * @return A copy of [this] with the updated bit
 */
fun Long.setBit(
    position: Int,
    value: Long,
): Long = this and (1L shl position).inv() or (value shl position)

/**
 * Get the value of the bit at the [position]
 *
 * @param position The position to get the bit value
 * @return 0 or 1
 */
@Suppress("unused")
fun Long.bitAt(position: Int): Long = (this shr position) and 1L

/**
 * Count the number of bits in the [Long]
 *
 * @return The number of bits
 */
fun Long.bitCount() =
    when (this) {
        0L -> 1
        in (-1_000_000_000..1_000_000_000) -> {
            var count = 0
            var abs = this.absoluteValue
            do {
                count += 1
                abs /= 10
            } while (abs > 0)
            count
        }
        else -> log10(this.absoluteValue.toDouble()).toInt() + 1
    }
