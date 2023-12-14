package helper

import java.io.File

enum class DataFile(val fileName: String) {
    Example("example.txt"),
    Example2("example_2.txt"),
    Part1("part_1.txt"),
    Part2("part_2.txt");
}


/**
 * Read the contents of the file into a string
 *
 * @param fileName The name of the file to read
 * @return The contents of the file as a string
 */
fun fileToString(fileName: String) = File(fileName).readText()

/**
 * Read the contents of the file into a string
 *
 * @param day The day number
 * @param fileType The [DataFile] corresponding to the file to read
 * @return The contents of the file as a string
 */
fun fileToString(day: Int, fileType: DataFile) = fileToString("src/main/resources/day_${day}/${fileType.fileName}")

/**
 * This is a utility for making reading large files easier when doing the actual solutions
 *
 * @param day The day number
 * @param fileType The [DataFile] corresponding to the file to read
 * @return An iterable sequence of strings
 */
fun fileToStream(day: Int, fileType: DataFile) = fileToStream("src/main/resources/day_${day}/${fileType.fileName}")

/**
 * This is a utility for making reading large files easier when doing the actual solutions
 *
 * @param fileName The file to read
 * @return An iterable sequence of strings
 */
fun fileToStream(fileName: String): Sequence<String> = File(fileName).inputStream().bufferedReader().lineSequence()
