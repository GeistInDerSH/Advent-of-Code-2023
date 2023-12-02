package helper

import java.io.File

fun fileToStream(fileName: String): Sequence<String> = File(fileName).inputStream().bufferedReader().lineSequence()