package com.geistindersh.aoc.helper.binary

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
