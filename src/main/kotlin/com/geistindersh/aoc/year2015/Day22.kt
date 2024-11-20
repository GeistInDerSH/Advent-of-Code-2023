package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day22(dataFile: DataFile) {
    private val boss =
        "[0-9]+"
            .toRegex()
            .findAll(fileToString(2015, 22, dataFile))
            .map { it.value.toInt() }
            .toList()
            .let { (hp, dmg) -> Boss(hp, dmg) }

    private data class Spell(
        val name: String,
        val cost: Int,
        val rounds: Int = 0,
        val onUse: (Player, Boss) -> Unit = { _, _ -> },
        val onProc: (Player, Boss) -> Unit = { _, _ -> },
        val onEnd: (Player, Boss) -> Unit = { _, _ -> },
    ) {
        companion object {
            val SPELLS =
                setOf(
                    Spell("Magic Missile", 53, onUse = { _, b -> b.hp -= 4 }),
                    Spell("Drain", 73, onUse = { p, b ->
                        p.hp += 2
                        b.hp -= 2
                    }),
                    Spell("Shield", 113, 6, onUse = { p, _ -> p.armor += 7 }, onEnd = { p, _ -> p.armor -= 7 }),
                    Spell("Poison", 173, 6, onProc = { _, b -> b.hp -= 3 }),
                    Spell("Recharge", 229, 5, onProc = { p, _ -> p.mp += 101 }),
                )
        }
    }

    private abstract class Character(open val hp: Int) {
        fun isAlive() = hp > 0
    }

    private data class Player(override var hp: Int, var mp: Int, var armor: Int = 0) : Character(hp) {
        fun hitBy(boss: Boss) {
            hp -= (boss.dmg - armor).coerceAtLeast(1)
        }
    }

    private data class Boss(override var hp: Int, val dmg: Int) : Character(hp)

    private data class Game(
        val player: Player,
        val boss: Boss,
        val isHardMode: Boolean,
        var manaSpent: Int = 0,
        val spellsInEffect: MutableList<Spell> = mutableListOf(),
    ) {
        fun bestOrNull(): Game? {
            var currentBest: Game? = null

            val queue = ArrayDeque<Game>().apply { add(this@Game) }
            while (queue.isNotEmpty()) {
                val game = queue.removeLast()
                if (currentBest != null && game.manaSpent > currentBest.manaSpent) continue

                for (spell in Spell.SPELLS) {
                    val nextGame = game.copy().next(spell) ?: continue
                    if (currentBest != null && nextGame.manaSpent > currentBest.manaSpent) continue

                    when {
                        nextGame.playerWins() -> currentBest = nextGame
                        nextGame.bossWins() -> continue
                        else -> queue.addLast(nextGame)
                    }
                }
            }

            return currentBest
        }

        private fun applySpells() {
            spellsInEffect.forEachIndexed { idx, spell ->
                if (spell.rounds > 0) spell.onProc(player, boss)
                if (spell.rounds == 1) spell.onEnd(player, boss)
                spellsInEffect[idx] = spell.copy(rounds = spell.rounds - 1)
            }
            spellsInEffect.removeIf { it.rounds <= 0 }
        }

        private fun next(spell: Spell): Game? {
            // Play Player's Turn
            if (isHardMode) player.hp -= 1
            if (!player.isAlive()) return this
            applySpells()
            if (!boss.isAlive()) return this
            if (player.mp < spell.cost) return null
            if (spellsInEffect.any { it.name == spell.name }) return null

            spell.onUse(player, boss)
            player.mp -= spell.cost
            manaSpent += spell.cost
            if (spell.rounds > 0) spellsInEffect.add(spell)

            // Play Boss Turn
            applySpells()
            if (!boss.isAlive()) return this
            player.hitBy(boss)
            return this
        }

        fun playerWins() = player.isAlive() && !boss.isAlive()

        fun bossWins() = boss.isAlive() && !player.isAlive()

        fun copy() = Game(player.copy(), boss.copy(), isHardMode, manaSpent, spellsInEffect.toMutableList())
    }

    fun part1(
        hp: Int,
        mp: Int,
    ) = Game(Player(hp, mp), boss, false)
        .bestOrNull()
        ?.manaSpent
        ?: throw Exception("No solution")

    fun part2(
        hp: Int,
        mp: Int,
    ) = Game(Player(hp, mp), boss, true)
        .bestOrNull()
        ?.manaSpent
        ?: throw Exception("No solution")
}

fun day22() {
    val day = Day22(DataFile.Part1)
    report(2015, 22, day.part1(50, 500), day.part2(50, 500))
}
