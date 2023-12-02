package day1

import helper.fileToStream
import helper.report

fun wordStringToInt(s: String): Int {
    return when (s) {
        "one", "1" -> 1
        "two", "2" -> 2
        "three", "3" -> 3
        "four", "4" -> 4
        "five", "5" -> 5
        "six", "6" -> 6
        "seven", "7" -> 7
        "eight", "8" -> 8
        "nine", "9" -> 9
        else -> 0
    }
}

fun solve(fileName: String, validValues: List<String>): Int {
    return fileToStream(fileName).sumOf {
        val first = wordStringToInt(it.findAnyOf(validValues)?.second ?: "0")
        val last = wordStringToInt(it.findLastAnyOf(validValues)?.second ?: "0")
        (first * 10) + last
    }
}

fun day1() {
    val validValuesNum = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    val validValuesNumAndStr = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "1", "2", "3", "4", "5", "6", "7", "8", "9"
    )
    report(
        dayNumber = 1,
        part1 = solve("src/main/resources/day_1/part_1.txt", validValuesNum),
        part2 = solve("src/main/resources/day_1/part_1.txt", validValuesNumAndStr)
    )
}