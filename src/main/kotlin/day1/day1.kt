package day1

import helper.fileToStream
import helper.report

fun part1(fileName: String): Int {
    val validValues = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    return fileToStream(fileName)
        .map {
            val first = it.findAnyOf(validValues)?.second ?: "0"
            val last = it.findLastAnyOf(validValues)?.second ?: "0"
            (first + last).toInt()
        }.toList().sum()
}

fun wordStringToNumeric(s: String): String {
    return when (s) {
        "one" -> "1"
        "two" -> "2"
        "three" -> "3"
        "four" -> "4"
        "five" -> "5"
        "six" -> "6"
        "seven" -> "7"
        "eight" -> "8"
        "nine" -> "9"
        else -> s
    }
}

fun part2(fileName: String): Int {
    val validValues = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "1", "2", "3", "4", "5", "6", "7", "8", "9"
    )
    return fileToStream(fileName).map {
        val first = wordStringToNumeric(it.findAnyOf(validValues)?.second ?: "0")
        val last = wordStringToNumeric(it.findLastAnyOf(validValues)?.second ?: "0")
        (first + last).toInt()
    }.toList().sum()
}

fun day1() {
    report(
        dayNumber = 1,
        part1 = part1("src/main/resources/day_1/part_1.txt"),
        part2 = part2("src/main/resources/day_1/part_1.txt")
    )
}