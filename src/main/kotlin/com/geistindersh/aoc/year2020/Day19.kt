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
            val (hasMatch, skip) = this.hasMatch(rules[0]!!)
            if (skip != this.length) return false
            return hasMatch
        }

        private fun String.hasMatch(gram: Grammar): Pair<Boolean, Int> {
            return when (gram) {
                is Grammar.Constant -> (this.first() == gram.value.first()) to gram.value.length
                is Grammar.OptionalRule -> {
                    val (lhsHasMatch, lhsSkip) = this.hasMatch(gram.lhs)
                    if (lhsHasMatch) return true to lhsSkip
                    val (rhsHasMatch, rhsSkip) = this.hasMatch(gram.rhs)
                    if (rhsHasMatch) return true to rhsSkip
                    false to 0
                }

                is Grammar.Rule -> {
                    var i = 0
                    for (ruleNumber in gram.rules) {
                        val rule = rules[ruleNumber]!!
                        val (isMatch, skip) = this.drop(i).hasMatch(rule)
                        if (!isMatch) return false to i
                        i += skip
                    }
                    true to i
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
    fun part2() = 0
}

fun day19() {
    val day = Day19(DataFile.Part1)
    report(2020, 19, day.part1(), day.part2())
}
