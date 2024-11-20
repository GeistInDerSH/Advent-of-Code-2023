package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*
import kotlin.math.ceil
import kotlin.math.max

class Day19(dataFile: DataFile) {
    private data class Robot(
        val oreCost: Int = 0,
        val clayCost: Int = 0,
        val obsidianCost: Int = 0,
        val oreRobots: Int = 0,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0,
    )

    private data class Blueprint(
        val id: Int,
        val oreRobot: Robot,
        val clayRobot: Robot,
        val obsidianRobot: Robot,
        val geodeRobot: Robot,
    ) {
        val maxOre = maxOf(oreRobot.oreCost, clayRobot.oreCost, obsidianRobot.oreCost, geodeRobot.oreCost)
        val maxClay = obsidianRobot.clayCost
        val maxObsidian = geodeRobot.obsidianCost
    }

    private val blueprints =
        fileToStream(2022, 19, dataFile)
            .map {
                val nums = "[0-9]+".toRegex().findAll(it).map { i -> i.value }.map(String::toInt).toList()
                val oreRobot = Robot(oreCost = nums[1], oreRobots = 1)
                val clayRobot = Robot(oreCost = nums[2], clayRobots = 1)
                val obsidianRobot = Robot(oreCost = nums[3], clayCost = nums[4], obsidianRobots = 1)
                val geodeRobot = Robot(oreCost = nums[5], obsidianCost = nums[6], geodeRobots = 1)

                Blueprint(nums[0], oreRobot, clayRobot, obsidianRobot, geodeRobot)
            }
            .toList()

    private data class State(
        val minutes: Int,
        val ore: Int = 1,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0,
        val oreRobots: Int = 1,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0,
    ) : Comparable<State> {
        override fun compareTo(other: State) = other.geode.compareTo(geode)

        fun next(robot: Robot): State {
            val minutes =
                robot.let {
                    val oreRemain = (robot.oreCost - ore).coerceAtLeast(0)
                    val maxOre = ceil(oreRemain / oreRobots.toFloat()).toInt()
                    val clayRemain = (robot.clayCost - clay).coerceAtLeast(0)
                    val maxClay = ceil(clayRemain / clayRobots.toFloat()).toInt()
                    val obsidianRemain = (robot.obsidianCost - obsidian).coerceAtLeast(0)
                    val maxObsidian = ceil(obsidianRemain / obsidianRobots.toFloat()).toInt()

                    1 + maxOf(maxOre, maxClay, maxObsidian)
                }

            return State(
                minutes = this.minutes - minutes,
                ore = ore + oreRobots * minutes - robot.oreCost,
                clay = clay + clayRobots * minutes - robot.clayCost,
                obsidian = obsidian + obsidianRobots * minutes - robot.obsidianCost,
                geode = geode + geodeRobots * minutes,
                oreRobots = oreRobots + robot.oreRobots,
                clayRobots = clayRobots + robot.clayRobots,
                obsidianRobots = obsidianRobots + robot.obsidianRobots,
                geodeRobots = geodeRobots + robot.geodeRobots,
            )
        }

        fun nextStates(blueprint: Blueprint) =
            buildList {
                if (blueprint.maxOre > oreRobots) add(next(blueprint.oreRobot))
                if (blueprint.maxClay > clayRobots) add(next(blueprint.clayRobot))
                if (clayRobots > 0 && blueprint.maxObsidian > obsidianRobots) add(next(blueprint.obsidianRobot))
                if (obsidianRobots > 0) add(next(blueprint.geodeRobot))
            }
                .filter { it.minutes > 0 }

        fun canOutproduce(best: Int) = (0..<minutes - 1).sumOf { it + geodeRobots } + geode > best
    }

    private fun best(
        blueprint: Blueprint,
        minutes: Int,
    ): Int {
        var best = 0
        val queue =
            PriorityQueue<State>()
                .apply { add(State(minutes = minutes)) }

        while (queue.isNotEmpty()) {
            val state = queue.poll()
            if (state.canOutproduce(best)) queue.addAll(state.nextStates(blueprint))
            best = max(best, state.geode + state.geodeRobots * (state.minutes - 1))
        }
        return best
    }

    fun part1() = blueprints.sumOf { it.id * best(it, 24) }

    fun part2() =
        blueprints
            .take(3)
            .map { best(it, 32) }
            .reduce(Int::times)
}

fun day19() {
    val day = Day19(DataFile.Part1)
    report(2022, 19, day.part1(), day.part2())
}
