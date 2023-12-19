package day19

import helper.DataFile
import helper.fileToString
import helper.report

typealias Condition = List<(Data) -> String>
typealias Workflow = Map<String, Condition>

data class Data(val map: Map<String, Long>) {
    fun sum() = map.values.sum()
}

fun parseInput(fileType: DataFile): Pair<Map<String, Condition>, List<Data>> {
    val (code, data) = fileToString(19, fileType).split("\n\n")

    val workflow = code.split('\n').associate { line ->
        val stage = line.substringBefore('{')
        val conditions = line.substringAfter('{').substringBefore('}').split(',').map { cond ->
            if (!cond.contains("[<>]".toRegex())) {
                { cond }
            } else {
                val delim = if ('<' in cond) '<' else '>'
                val key = cond.substringBefore(delim)
                val number = cond.substringAfter(delim).substringBefore(':').toLong()
                val dest = cond.substringAfter(':')

                if (delim == '<') {
                    { d: Data -> if (key in d.map && d.map[key]!! < number) dest else "" }
                } else {
                    { d: Data -> if (key in d.map && d.map[key]!! > number) dest else "" }
                }
            }
        }
        stage to conditions
    }

    val parsedData = data.split('\n').map { line ->
        val parts = line.substring(1, line.length - 1).split(',').associate {
            val (k, v) = it.split('=')
            k to v.toLong()
        }
        Data(parts)
    }

    return workflow to parsedData
}

fun part1(workflow: Workflow, data: List<Data>): Long {
    return data.sumOf { value ->
        var workflowName = "in"
        while (workflowName !in setOf("A", "R")) {
            workflowName = workflow[workflowName]!!.map { it(value) }.first { it != "" }
        }
        if (workflowName == "A") value.sum()
        else 0
    }
}

fun day19() {
    val (workflow, data) = parseInput(DataFile.Part1)
    report(
        dayNumber = 19,
        part1 = part1(workflow, data),
        part2 = "",
    )
}