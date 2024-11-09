package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day19(dataFile: DataFile) {
    private val grammar = fileToString(2020, 19, dataFile).let { data ->
        val (rules, input) = data.split("\n\n")
        val ruleMap = rules.split("\n").associate { Grammar.from(it) }
        Message(ruleMap, input.split("\n"))
    }

    private data class Message(val rules: Map<Int, Grammar>, val input: List<String>) {
        private fun String.hasMatch(): Boolean {
            val strings = this.hasMatch(rules[0]!!)
            return strings.any { it.isEmpty() }
        }

        private fun String.hasMatch(gram: Grammar): List<String> {
            if (this.isEmpty()) return emptyList()

            return when (gram) {
                is Grammar.Constant -> if (this.first() == gram.value.first()) {
                    listOf(this.drop(1))
                } else {
                    emptyList()
                }

                is Grammar.OptionalRule -> {
                    val lhs = this.hasMatch(gram.lhs)
                    val rhs = this.hasMatch(gram.rhs)
                    lhs + rhs
                }

                is Grammar.Rule -> {
                    var found = listOf(this)
                    for (ruleNumber in gram.rules) {
                        val rule = rules[ruleNumber]!!
                        val new = found.flatMap { it.hasMatch(rule) }
                        if (new.isEmpty()) return emptyList()
                        found = new
                    }
                    found
                }
            }
        }

        fun matchCount() = input.count { it.hasMatch() }
    }

    private sealed class Grammar {
        data class Constant(val value: String) : Grammar()
        data class Rule(val rules: List<Int>) : Grammar()
        data class OptionalRule(val lhs: Rule, val rhs: Rule) : Grammar()

        companion object {
            fun from(line: String): Pair<Int, Grammar> {
                val (rule, body) = line.split(": ")
                val gram = if (body.contains('"')) {
                    Constant(body.replace("\"", ""))
                } else if (body.contains('|')) {
                    val (lhs, rhs) = body
                        .split("|")
                        .map { text -> text.trim().split(" ").map { it.toInt() } }
                        .map { Rule(it) }
                    OptionalRule(lhs, rhs)
                } else {
                    Rule(body.split(" ").map { it.toInt() })
                }

                return rule.toInt() to gram
            }
        }
    }

    fun part1() = grammar.matchCount()
    fun part2() = grammar
        .copy(rules = grammar
            .rules
            .toMutableMap()
            .apply {
                set(8, Grammar.OptionalRule(Grammar.Rule(listOf(42)), Grammar.Rule(listOf(42, 8))))
                set(11, Grammar.OptionalRule(Grammar.Rule(listOf(42, 31)), Grammar.Rule(listOf(42, 11, 31))))

            }
        )
        .matchCount()
}

fun day19() {
    val day = Day19(DataFile.Part1)
    report(2020, 19, day.part1(), day.part2())
}
