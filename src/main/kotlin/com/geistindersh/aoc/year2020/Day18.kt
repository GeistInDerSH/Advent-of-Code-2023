package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*

class Day18(dataFile: DataFile) {
    private val equations = fileToStream(2020, 18, dataFile)
        .map { Token.tokenize(it) }
        .map { Equation(it) }
        .toList()

    private data class Equation(val tokens: List<Token>) {
        fun eval(): Long {
            val stack = Stack<Token>()
            for (token in tokens) {
                when {
                    stack.isEmpty() -> stack.push(token)
                    token is Token.Multiply -> stack.push(token)
                    token is Token.Add -> stack.push(token)
                    token is Token.OpenParentheses -> stack.push(token)
                    token is Token.ClosedParentheses -> {
                        while (true) {
                            val num = stack.pop()
                            when (val op = stack.peek()) {
                                is Token.Add, is Token.Multiply -> {
                                    stack.pop()
                                    val n2 = stack.pop() as Token.Number
                                    val new = (op as Call).eval(num as Token.Number, n2)
                                    stack.push(new)
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

                    token is Token.Number -> {
                        when (val head = stack.peek()) {
                            is Token.ClosedParentheses -> throw IllegalStateException(") NUM")
                            is Token.Number -> throw IllegalStateException("NUM NUM")
                            is Token.OpenParentheses -> stack.push(token)
                            is Token.Multiply, is Token.Add -> {
                                stack.pop()
                                val n2 = stack.pop() as Token.Number
                                val reduced = (head as Call).eval(token, n2)
                                stack.push(reduced)
                            }
                        }
                    }
                }

                // Reduce existing stack
                while (true) {
                    if (stack.isEmpty()) break
                    val head = stack.pop()
                    if (stack.isEmpty() || head !is Token.Number) {
                        stack.push(head)
                        break
                    }
                    val next = stack.peek()
                    stack.push(head)

                    when (next) {
                        is Token.Add, is Token.Multiply -> {
                            stack.pop()
                            val op = stack.pop() as Call
                            val n2 = stack.pop() as Token.Number
                            val new = op.eval(head, n2)
                            stack.push(new)
                        }

                        is Token.ClosedParentheses -> break
                        is Token.OpenParentheses -> break
                        is Token.Number -> break
                    }
                }
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
                    token is Token.Multiply -> stack.push(token)
                    token is Token.Add -> stack.push(token)
                    token is Token.OpenParentheses -> stack.push(token)
                    token is Token.ClosedParentheses -> {
                        while (true) {
                            val num = stack.pop()
                            when (val op = stack.peek()) {
                                is Token.Add, is Token.Multiply -> {
                                    stack.pop()
                                    val n2 = stack.pop() as Token.Number
                                    val new = (op as Call).eval(num as Token.Number, n2)
                                    stack.push(new)
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

                    token is Token.Number -> {
                        when (val head = stack.peek()) {
                            is Token.ClosedParentheses -> throw IllegalStateException(") NUM")
                            is Token.Number -> throw IllegalStateException("NUM NUM")
                            is Token.OpenParentheses -> stack.push(token)
                            is Token.Multiply -> stack.push(token)
                            is Token.Add -> {
                                stack.pop()
                                val n2 = stack.pop() as Token.Number
                                val reduced = (head as Call).eval(token, n2)
                                stack.push(reduced)
                            }
                        }
                    }
                }

                // Reduce existing stack, but only additions
                while (true) {
                    if (stack.isEmpty()) break
                    val head = stack.pop()
                    if (stack.isEmpty() || head !is Token.Number) {
                        stack.push(head)
                        break
                    }
                    val next = stack.peek()
                    stack.push(head)

                    when (next) {
                        is Token.Add -> {
                            stack.pop()
                            val op = stack.pop() as Call
                            val n2 = stack.pop() as Token.Number
                            val new = op.eval(head, n2)
                            stack.push(new)
                        }

                        else -> break
                    }
                }
            }

            // One last reduction of the stack
            while (true) {
                if (stack.isEmpty()) break
                val head = stack.pop()
                if (stack.isEmpty() || head !is Token.Number) {
                    stack.push(head)
                    break
                }
                val next = stack.peek()
                stack.push(head)

                when (next) {
                    is Token.Add, is Token.Multiply -> {
                        stack.pop()
                        val op = stack.pop() as Call
                        val n2 = stack.pop() as Token.Number
                        val new = op.eval(head, n2)
                        stack.push(new)
                    }

                    is Token.ClosedParentheses -> break
                    is Token.OpenParentheses -> break
                    is Token.Number -> break
                }
            }
            val head = stack.pop()
            if (head !is Token.Number) throw IllegalStateException("Expected NUMBER")
            return head.value
        }
    }

    private interface Call {
        fun eval(n1: Token.Number, n2: Token.Number): Token.Number
    }

    private sealed class Token {
        data class Number(val value: Long) : Token()
        data class OpenParentheses(val tkn: Char = '(') : Token()
        data class ClosedParentheses(val tkn: Char = ')') : Token()
        data class Multiply(val tkn: Char = '*') : Token(), Call {
            override fun eval(n1: Number, n2: Number) = Number(n1.value * n2.value)
        }

        data class Add(val tkn: Char = '+') : Token(), Call {
            override fun eval(n1: Number, n2: Number) = Number(n1.value + n2.value)
        }


        companion object {
            fun tokenize(raw: String): List<Token> {
                val tokens = mutableListOf<Token>()
                var i = 0
                var num: Long? = null
                while (i < raw.length) {
                    val char = raw[i]
                    if (char.isDigit()) {
                        if (num == null) {
                            num = 0
                        }

                        num *= 10
                        num += char.digitToInt()
                        i += 1
                        continue
                    } else if (num != null) {
                        tokens.add(Number(num))
                        num = null
                        continue
                    }

                    val tkn = when (char) {
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
                if (num != null) {
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
