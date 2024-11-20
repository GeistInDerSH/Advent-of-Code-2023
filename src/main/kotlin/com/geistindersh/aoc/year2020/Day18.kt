package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.Stack

class Day18(
    dataFile: DataFile,
) {
    private val equations =
        fileToStream(2020, 18, dataFile)
            .map { Token.tokenize(it) }
            .map { Equation(it) }
            .toList()

    private data class Equation(
        val tokens: List<Token>,
    ) {
        private fun Stack<Token>.reduceAll() = this.reduce(setOf(Token.Add(), Token.Multiply()))

        private fun Stack<Token>.reduceAdditions() = this.reduce(setOf(Token.Add()))

        /**
         * Reduce the values on the stack when a given [Call] operation from
         * [operations] can be preformed.
         *
         * @param operations The operations that we can perform to reduce the stack
         */
        private fun Stack<Token>.reduce(operations: Set<Call>) {
            val stack = this
            while (true) {
                if (stack.isEmpty()) break
                val head = stack.pop()
                if (stack.isEmpty() || head !is Token.Number) {
                    stack.push(head)
                    break
                }
                val next = stack.peek()
                stack.push(head)

                if (next !is Call) break
                if (next !in operations) break
                stack.pop()
                val op = stack.pop() as Call
                val n2 = stack.pop() as Token.Number
                val new = op.eval(head, n2)
                stack.push(new)
            }
        }

        private fun Stack<Token>.reduceWhileInParentheses() {
            val stack = this
            while (true) {
                val num = stack.pop()
                when (val op = stack.peek()) {
                    is Token.Add, is Token.Multiply -> {
                        stack.push(num)
                        stack.reduceAll()
                    }

                    is Token.OpenParentheses -> {
                        stack.pop()
                        stack.push(num)
                        break
                    }

                    else -> throw IllegalArgumentException(op.toString())
                }
            }
        }

        fun eval(): Long {
            val stack = Stack<Token>()
            for (token in tokens) {
                when {
                    stack.isEmpty() -> stack.push(token)
                    token is Token.ClosedParentheses -> stack.reduceWhileInParentheses()
                    token is Token.Number -> {
                        val head = stack.peek()
                        stack.push(token)
                        when (head) {
                            is Token.ClosedParentheses -> throw IllegalStateException(") NUM")
                            is Token.Number -> throw IllegalStateException("NUM NUM")
                            is Token.Multiply, is Token.Add -> stack.reduceAll()
                            else -> continue
                        }
                    }

                    else -> stack.push(token)
                }

                // Reduce existing stack
                stack.reduceAll()
            }
            val head = stack.pop()
            if (head !is Token.Number) throw IllegalStateException("Expected NUMBER")
            return head.value
        }

        fun evalWithPrecedence(): Long {
            val stack = Stack<Token>()
            for (token in tokens) {
                when {
                    stack.isEmpty() -> stack.push(token)
                    token is Token.ClosedParentheses -> stack.reduceWhileInParentheses()
                    token is Token.Number -> {
                        val head = stack.peek()
                        stack.push(token)
                        when (head) {
                            is Token.ClosedParentheses -> throw IllegalStateException(") NUM")
                            is Token.Number -> throw IllegalStateException("NUM NUM")
                            is Token.Add -> stack.reduceAdditions()
                            else -> continue
                        }
                    }

                    else -> stack.push(token)
                }

                // Reduce existing stack, but only additions
                stack.reduceAdditions()
            }

            // One last reduction of the stack
            stack.reduceAll()
            val head = stack.pop()
            if (head !is Token.Number) throw IllegalStateException("Expected NUMBER")
            return head.value
        }
    }

    private interface Call {
        fun eval(
            n1: Token.Number,
            n2: Token.Number,
        ): Token.Number
    }

    private sealed class Token {
        data class Number(
            val value: Long,
        ) : Token()

        data class OpenParentheses(
            val tkn: Char = '(',
        ) : Token()

        data class ClosedParentheses(
            val tkn: Char = ')',
        ) : Token()

        data class Multiply(
            val tkn: Char = '*',
        ) : Token(),
            Call {
            override fun eval(
                n1: Number,
                n2: Number,
            ) = Number(n1.value * n2.value)
        }

        data class Add(
            val tkn: Char = '+',
        ) : Token(),
            Call {
            override fun eval(
                n1: Number,
                n2: Number,
            ) = Number(n1.value + n2.value)
        }

        companion object {
            fun tokenize(raw: String): List<Token> {
                val tokens = mutableListOf<Token>()
                var i = 0
                var num: Long? = null
                while (i < raw.length) {
                    val char = raw[i]
                    if (char.isDigit()) {
                        if (num == null) { // Start of number
                            num = 0
                        }

                        num *= 10
                        num += char.digitToInt()
                        i += 1
                        continue
                    } else if (num != null) {
                        // Next token is not a number, and we have one saved
                        tokens.add(Number(num))
                        num = null
                        continue
                    }

                    val tkn =
                        when (char) {
                            '(' -> OpenParentheses()
                            ')' -> ClosedParentheses()
                            '*' -> Multiply()
                            '+' -> Add()
                            else -> {
                                i += 1
                                continue
                            }
                        }

                    tokens.add(tkn)
                    i += 1
                }
                if (num != null) { // We have a number saved, but not pushed back
                    tokens.add(Number(num))
                }

                return tokens
            }
        }
    }

    fun part1() = equations.sumOf { it.eval() }

    fun part2() = equations.sumOf { it.evalWithPrecedence() }
}

fun day18() {
    val day = Day18(DataFile.Part1)
    report(2020, 18, day.part1(), day.part2())
}
