package com.geistindersh.aoc.helper.files

import kotlin.io.path.Path
import kotlin.io.path.inputStream
import kotlin.io.path.readText

enum class DataFile(
    private val fileName: String,
) {
    Example("example.txt"),
    Example2("example_2.txt"),
    Part1("part_1.problem.txt"),
    ;

    fun filePath(
        year: Int,
        day: Int,
    ) = "src/main/resources/$year/$day/$fileName"
}

/**
 * Read the contents of the file into a string
 *
 * @param fileName The name of the file to read
 * @return The contents of the file as a string
 */
fun fileToString(fileName: String) = Path(fileName).readText()

/**
 * Read the contents of the file into a string
 *
 * @param year The year number
 * @param day The day number
 * @param fileType The [DataFile] corresponding to the file to read
 * @return The contents of the file as a string
 */
fun fileToString(
    year: Int,
    day: Int,
    fileType: DataFile,
) = fileToString(fileType.filePath(year, day))

/**
 * This is a utility for making reading large files easier when doing the actual solutions
 *
 * @param year The year number
 * @param day The day number
 * @param fileType The [DataFile] corresponding to the file to read
 * @return An iterable sequence of strings
 */
fun fileToStream(
    year: Int,
    day: Int,
    fileType: DataFile,
) = fileToStream(fileType.filePath(year, day))

/**
 * This is a utility for making reading large files easier when doing the actual solutions
 *
 * @param fileName The file to read
 * @return An iterable sequence of strings
 */
fun fileToStream(fileName: String) =
    Path(fileName)
        .inputStream()
        .bufferedReader()
        .lineSequence()
