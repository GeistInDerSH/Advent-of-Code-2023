package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

class Day18(dataFile: DataFile) {
	private val data = fileToStream(2021, 18, dataFile).map(::parseLine).map { Sailfish.from(it) }.toList()

	private fun parseLine(line: String): List<Any> {
		val stack = Stack<MutableList<Any>>().apply { push(mutableListOf()) }
		for (char in line) {
			when (char) {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
					stack.peek().add(char.digitToInt().toLong())
				}

				'[' -> stack.push(mutableListOf())
				']' -> {
					val top = stack.pop()
					stack.peek().add(top)
				}

				else -> continue
			}
		}
		val listStart = stack.firstElement().first()
		if (listStart !is MutableList<*>) throw IllegalArgumentException("The stack produced an invalid object: $listStart")
		return listStart as List<Any>
	}

	sealed class Sailfish(var parent: Group? = null) {
		data class Integer(var value: Long) : Sailfish() {
			override fun toString() = value.toString()

			fun split() {
				val left = floor(value / 2.0).toLong()
				val right = ceil(value / 2.0).toLong()
				val group = Group(Integer(left), Integer(right))
				this.parent?.update(this, group)
			}
		}

		data class Group(var left: Sailfish, var right: Sailfish) : Sailfish() {
			init {
				left.parent = this
				right.parent = this
			}

			override fun toString() = "[${left}, ${right}]"

			fun update(old: Sailfish, new: Sailfish) {
				if (left === old) {
					left = new
				} else {
					right = new
				}
				new.parent = this
			}
		}

		private fun findExplode(depth: Int = 1): Sailfish? {
			return when (this) {
				is Integer -> null
				is Group -> {
					if (depth == 5) {
						this
					} else {
						this.left.findExplode(depth + 1) ?: this.right.findExplode(depth + 1)
					}
				}
			}
		}

		private fun explode() {
			when (this) {
				is Integer -> return
				is Group -> {
					val current = this
					firstParentOnSide(Group::left)?.left?.rightMostInteger()?.apply {
						value += (current.left as Integer).value
					}
					firstParentOnSide(Group::right)?.right?.leftMostInteger()?.apply {
						value += (current.right as Integer).value
					}

					this.parent?.update(this, Integer(0))
				}
			}
		}

		private fun findSplit(): Integer? {
			return when (this) {
				is Integer -> if (this.value >= 10) this else null
				is Group -> this.left.findSplit() ?: this.right.findSplit()
			}
		}

		private fun rightMostInteger(): Integer {
			return when (this) {
				is Integer -> this
				is Group -> this.right.rightMostInteger()
			}
		}

		private fun leftMostInteger(): Integer {
			return when (this) {
				is Integer -> this
				is Group -> this.left.leftMostInteger()
			}
		}

		private fun firstParentOnSide(side: Group.() -> Sailfish): Group? {
			var current = this
			while (current.parent != null) {
				if (current.parent!!.side() !== current) {
					return current.parent
				} else {
					current = current.parent!!
				}
			}
			return null
		}

		fun reduce(): Sailfish {
			while (true) {
				val canExplode = this.findExplode()
				if (canExplode != null) {
					canExplode.explode()
					continue
				}
				val canSplit = this.findSplit()
				if (canSplit != null) {
					canSplit.split()
					continue
				}
				break
			}
			return this
		}

		fun magnitude(): Long = when (this) {
			is Integer -> value
			is Group -> 3 * left.magnitude() + 2 * right.magnitude()
		}

		companion object {
			fun from(list: List<Any>): Sailfish {
				val first = list.first()
				val left = if (first is MutableList<*>) from(first as List<Any>) else Integer(first as Long)
				val last = list.last()
				val right = if (last is MutableList<*>) from(last as List<Any>) else Integer(last as Long)
				val group = Group(left, right)
				left.parent = group
				right.parent = group
				return group
			}
		}
	}

	fun part1() = data
		.reduce { acc, sailfish -> Sailfish.Group(acc, sailfish).reduce() }
		.magnitude()


	fun part2() = 0
}

fun day18() {
	val day = Day18(DataFile.Part1)
	report(2021, 18, day.part1(), day.part2())
}
