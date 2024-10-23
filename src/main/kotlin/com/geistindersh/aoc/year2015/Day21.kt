package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.cycle
import com.geistindersh.aoc.helper.iterators.pairCombinations
import com.geistindersh.aoc.helper.report

class Day21(dataFile: DataFile) {
	private val boss = "[0-9]+"
		.toRegex()
		.findAll(fileToString(2015, 21, dataFile))
		.map { it.value.toInt() }
		.toList()
		.let { (hp, dmg, amr) -> Character(hp, dmg, amr, 0) }
	private val shop = mapOf(
		"weapons" to listOf(
			Item("Dagger", 8, 4, 0),
			Item("Shortsword", 10, 5, 0),
			Item("Warhammer", 25, 6, 0),
			Item("Longsword", 40, 7, 0),
			Item("Greataxe", 74, 8, 0),
		),
		"armor" to listOf(
			Item("Leather", 13, 0, 1),
			Item("Chainmail", 31, 0, 2),
			Item("Splintmail", 53, 0, 3),
			Item("Bandedmail", 75, 0, 4),
			Item("Platemail", 102, 0, 5),
		),
		"ring" to listOf(
			Item("Damage +1", 25, 1, 0),
			Item("Damage +2", 50, 2, 0),
			Item("Damage +3", 100, 3, 0),
			Item("Defense +1", 20, 0, 1),
			Item("Defense +2", 40, 0, 2),
			Item("Defense +3", 80, 0, 3),
		)
	)

	private fun generateCharacters(hp: Int) = sequence {
		for (weapon in shop["weapons"]!!) {
			val armorOptions = shop["armor"]!! + null
			val ringOptions = shop["ring"]!!.pairCombinations() +
					shop["ring"]!!.asSequence().zip(listOf(null).cycle()) +
					sequenceOf(Pair(null, null))

			val baseDmg = weapon.dmg
			val baseCost = weapon.cost

			for (armorOption in armorOptions) {
				val baseAmr = armorOption?.amr ?: 0
				val costWithArmor = baseCost + (armorOption?.cost ?: 0)

				for (ringOption in ringOptions) {
					val ringList = ringOption.toList()
					val dmg = baseDmg + ringList.map { it?.dmg ?: 0 }.reduce(Int::plus)
					val amr = baseAmr + ringList.map { it?.amr ?: 0 }.reduce(Int::plus)
					val cost = costWithArmor + ringList.map { it?.cost ?: 0 }.reduce(Int::plus)

					yield(Character(hp, dmg, amr, cost))
				}
			}
		}
	}

	private data class Item(val name: String, val cost: Int, val dmg: Int, val amr: Int)
	private data class Character(val hp: Int, val dmg: Int, val amr: Int, val cost: Int) {
		val isAlive = hp > 0

		fun hit(other: Character) =
			Character(other.hp - (dmg - other.amr).coerceAtLeast(1), other.dmg, other.amr, other.cost)
	}

	private fun isWinner(player: Character): Boolean {
		var pc = player
		var boss = boss
		while (true) {
			boss = pc.hit(boss)
			if (!boss.isAlive) return true

			pc = boss.hit(pc)
			if (!pc.isAlive) return false
		}
	}

	fun part1(hp: Int) = generateCharacters(hp)
		.filter(::isWinner)
		.minBy { it.cost }
		.cost

	fun part2(hp: Int) = generateCharacters(hp)
		.filterNot(::isWinner)
		.maxBy { it.cost }
		.cost
}

fun day21() {
	val day = Day21(DataFile.Part1)
	report(2015, 21, day.part1(100), day.part2(100))
}
