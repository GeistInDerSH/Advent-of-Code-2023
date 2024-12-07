package com.geistindersh.aoc.helper.binary

import kotlin.math.pow

/**
 * Concatenate two numbers and return the value as a number.
 *
 * If the two numbers are not of the same type, the return type is the larger container size of the two,
 * e.g. Int and Long returns a Long, Int and Int returns an Int.
 *
 * ```kotlin
 * 12.concat(345) == 12345
 * ```
 * @param other The other number to concat
 * @return The two numbers concatenated
 */
fun Long.concat(other: Long) = this * 10.0.pow(other.bitCount()).toLong() + other

/**
 * Concatenate two numbers and return the value as a number.
 *
 * If the two numbers are not of the same type, the return type is the larger container size of the two,
 * e.g. Int and Long returns a Long, Int and Int returns an Int.
 *
 * ```kotlin
 * 12.concat(345) == 12345
 * ```
 * @param other The other number to concat
 * @return The two numbers concatenated
 */
@Suppress("unused")
fun Long.concat(other: Int) = this.concat(other.toLong())

/**
 * Concatenate two numbers and return the value as a number.
 *
 * If the two numbers are not of the same type, the return type is the larger container size of the two,
 * e.g. Int and Long returns a Long, Int and Int returns an Int.
 *
 * ```kotlin
 * 12.concat(345) == 12345
 * ```
 * @param other The other number to concat
 * @return The two numbers concatenated
 */
@Suppress("unused")
fun Int.concat(other: Int) = this * 10.0f.pow(other.bitCount()).toInt() + other

/**
 * Concatenate two numbers and return the value as a number.
 *
 * If the two numbers are not of the same type, the return type is the larger container size of the two,
 * e.g. Int and Long returns a Long, Int and Int returns an Int.
 *
 *
 * ```kotlin
 * 12.concat(345) == 12345
 * ```
 * @param other The other number to concat
 * @return The two numbers concatenated
 */
@Suppress("unused")
fun Int.concat(other: Long) = this.toLong().concat(other)
