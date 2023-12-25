package day1

import helper.files.DataFile
import helper.files.fileToStream
import helper.report

/**
 * Attempt to convert the given string into a numeric value. If it does not match one of the
 * supported values, return 0 as to not impact the solution.
 *
 * @return The numeric representation of the string
 */
fun String.wordStringToInt(): Int {
    return when (this) {
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

/**
 * Read through each line of the file, finding the first and last instance that is in
 * the [validValues] param, and return the sum of each line.
 *
 * @param fileType The file to read the input from
 * @param validValues A list of valid matches for the numbers
 * @return The sum of the found values
 */
fun solve(fileType: DataFile, validValues: List<String>): Int {
    return fileToStream(1, fileType).sumOf {
        val first = (it.findAnyOf(validValues)?.second ?: "0").wordStringToInt()
        val last = (it.findLastAnyOf(validValues)?.second ?: "0").wordStringToInt()
        (first * 10) + last
    }
}

fun day1() {
    // part1 only allows for numbers to be parsed out from the strings
    val validValuesNum = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    // part2 allows for numbers, or the text version of the numbers to represent the number
    val validValuesNumAndStr = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "1", "2", "3", "4", "5", "6", "7", "8", "9"
    )
    report(
        dayNumber = 1,
        part1 = solve(DataFile.Part1, validValuesNum),
        part2 = solve(DataFile.Part1, validValuesNumAndStr)
    )
}