package com.geistindersh.aoc.helper.strings

private val NUMBER_REGEX = "-?[0-9]+".toRegex()
private val POSITIVE_NUMBER_REGEX = "[0-9]+".toRegex()

/**
 * Extract all positive and negative numbers from the string
 *
 * @return A list of [Int] from the [String]
 */
@Suppress("unused")
fun String.extractIntegers() = NUMBER_REGEX.findAll(this).map { it.value.toInt() }.toList()

/**
 * Extract all positive numbers from the string
 *
 * @return A list of [Int] from the [String]
 */
@Suppress("unused")
fun String.extractPositiveIntegers() = POSITIVE_NUMBER_REGEX.findAll(this).map { it.value.toInt() }.toList()

/**
 * Extract all positive and negative numbers from the string
 *
 * @return A list of [Long] from the [String]
 */
@Suppress("unused")
fun String.extractLongs() = NUMBER_REGEX.findAll(this).map { it.value.toLong() }.toList()

/**
 * Extract all positive numbers from the string
 *
 * @return A list of [Long] from the [String]
 */
@Suppress("unused")
fun String.extractPositiveLongs() = POSITIVE_NUMBER_REGEX.findAll(this).map { it.value.toLong() }.toList()
