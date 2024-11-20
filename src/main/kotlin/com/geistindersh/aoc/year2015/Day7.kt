package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day7(dataFile: DataFile) {
    private val operations =
        fileToStream(2015, 7, dataFile)
            .map { Operation.from(it) }
            .toList()

    private sealed class Operation(open val name: String) {
        data class Value(override val name: String, val value: UShort) : Operation(name)

        data class UnprocessedValue(override val name: String, val value: String) : Operation(name)

        data class And(override val name: String, val lhs: String, val rhs: String) : Operation(name)

        data class Or(override val name: String, val lhs: String, val rhs: String) : Operation(name)

        data class Not(override val name: String, val lhs: String) : Operation(name)

        data class RShift(override val name: String, val lhs: String, val amount: Int) : Operation(name)

        data class LShift(override val name: String, val lhs: String, val amount: Int) : Operation(name)

        companion object {
            fun from(string: String): Operation {
                val parts = string.split(" ")
                val name = parts.last()
                return when (parts[1]) {
                    "AND" -> And(name, parts[0], parts[2])
                    "OR" -> Or(name, parts[0], parts[2])
                    "LSHIFT" -> LShift(name, parts[0], parts[2].toInt())
                    "RSHIFT" -> RShift(name, parts[0], parts[2].toInt())
                    "->" -> UnprocessedValue(name, parts[0])
                    else -> Not(name, parts[1])
                }
            }
        }
    }

    private fun eval(
        expressions: MutableMap<String, Operation>,
        name: String,
    ): Operation.Value {
        return if (name.toUShortOrNull() != null) {
            Operation.Value(name, name.toUShort())
        } else {
            eval(expressions, expressions[name]!!)
        }
    }

    private fun eval(
        expressions: MutableMap<String, Operation>,
        operation: Operation,
    ): Operation.Value {
        val value =
            when (operation) {
                is Operation.And -> {
                    val lhs = eval(expressions, operation.lhs)
                    val rhs = eval(expressions, operation.rhs)
                    val value = lhs.value and rhs.value
                    Operation.Value(operation.name, value)
                }

                is Operation.Not -> {
                    val value = eval(expressions, operation.lhs)
                    Operation.Value(operation.name, value.value.inv())
                }

                is Operation.Or -> {
                    val lhs = eval(expressions, operation.lhs)
                    val rhs = eval(expressions, operation.rhs)
                    val value = lhs.value or rhs.value
                    Operation.Value(operation.name, value)
                }

                is Operation.LShift -> {
                    val lhs = eval(expressions, operation.lhs)
                    val shifted = lhs.value.toInt() shl operation.amount
                    Operation.Value(operation.name, shifted.toUShort())
                }

                is Operation.RShift -> {
                    val lhs = eval(expressions, operation.lhs)
                    val shifted = lhs.value.toInt() shr operation.amount
                    Operation.Value(operation.name, shifted.toUShort())
                }

                is Operation.UnprocessedValue -> eval(expressions, operation.value)
                is Operation.Value -> operation
            }
        expressions[operation.name] = value
        return value
    }

    fun part1(wire: String): UShort {
        val expressions = operations.associateBy { it.name }.toMutableMap()
        return eval(expressions, wire).value
    }

    fun part2(wire: String): UShort {
        val expressions = operations.associateBy { it.name }.toMutableMap()
        val wireValue = eval(expressions, wire)
        val updatedExpressions = operations.associateBy { it.name }.toMutableMap().apply { set("b", wireValue) }
        return eval(updatedExpressions, wire).value
    }
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2015, 7, day.part1("a"), day.part2("a"))
}
