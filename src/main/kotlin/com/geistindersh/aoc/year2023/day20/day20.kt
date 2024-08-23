package com.geistindersh.aoc.year2023.day20

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.math.lcm
import com.geistindersh.aoc.helper.report

enum class ModuleType {
    Broadcast,
    FlipFlop,
    Conjunction;

    override fun toString(): String {
        return when (this) {
            Broadcast -> "B"
            FlipFlop -> "%"
            Conjunction -> "&"
        }
    }
}


data class Module(val name: String, val moduleType: ModuleType, val targets: List<String>) {
    private var pulse = false
    private val memory = mutableMapOf<String, Boolean>()

    fun reset() {
        pulse = false
        memory.clear()
    }

    fun pulseIsHigh() = pulse
    fun invertPulse() {
        pulse = !pulse
    }

    /**
     * Add a list of sources to the memory to track, with their current pulse being false
     *
     * @param sources The sources to add
     */
    fun initializeMemory(sources: List<String>) {
        sources.forEach { memory[it] = false }
    }

    /**
     * @param module The module to use to update the [memory] with
     */
    fun updateMemory(module: Module) = updateMemory(module.name, module.pulse)
    private fun updateMemory(name: String, value: Boolean) {
        memory[name] = value
        pulse = !memory.values.all { it }
    }

    companion object {
        fun fromString(string: String): Module {
            val (start, moduleString) = string.split(" -> ")
            val targets = moduleString.split(", ")
            return when (start[0]) {
                '%' -> Module(start.substring(1), ModuleType.FlipFlop, targets)
                '&' -> Module(start.substring(1), ModuleType.Conjunction, targets)
                else -> Module(start, ModuleType.Broadcast, targets)
            }
        }
    }
}

data class Propagation(val modules: List<Module>) {
    private val initialTargets by lazy {
        modules
            .first { it.name == "broadcaster" }
            .let { start -> start.targets.map { start to it } }
    }

    /**
     * Reset the memory and pulse status for each of the [modules], then re-initialize memory for Conjunction modules
     */
    private fun reset() {
        modules.forEach { it.reset() }

        // Initialize the memory of all Conjunction modules, by getting values that will write to the module
        // and storing the current pulse of the module to the Conjunction's memory
        modules
            .filter { it.moduleType == ModuleType.Conjunction }
            .forEach { module ->
                val inputs = modules
                    .filter { module.name in it.targets }
                    .map { it.name }
                module.initializeMemory(inputs)
            }
    }

    /**
     * Recursively go through the [queue], plucking the first entry off and processing it.
     *
     * @param queue A list of modules to process. The first is the source module, the second is the target
     * @param lowCount The number of low pulses seen
     * @param highCount The number of high pulses seen
     * @return A pair of [lowCount] to [highCount]
     *
     * @see part2 For an iterative implementation of this same function
     */
    private tailrec fun part1(queue: List<Pair<Module, String>>, lowCount: Long, highCount: Long): Pair<Long, Long> {
        if (queue.isEmpty()) {
            return lowCount to highCount
        }

        val (source, destName) = queue.first()
        val remainingQueue = queue.drop(1)
        val (high, low) = if (source.pulseIsHigh()) {
            highCount + 1 to lowCount
        } else {
            highCount to lowCount + 1
        }

        // Get the actual module we are targeting
        val destination = modules.firstOrNull { it.name == destName } ?: return part1(remainingQueue, low, high)

        when (destination.moduleType) {
            // Update the memory with of the Conjunction module with the source name and pulse. Then update the
            // pulse of the Conjunction module
            ModuleType.Conjunction -> destination.updateMemory(source)

            // Continue on to the next, should not be reachable
            ModuleType.Broadcast -> return part1(remainingQueue, low, high)

            // Continue on to the next if the pulse is already high, otherwise invert the pulse
            ModuleType.FlipFlop -> {
                if (source.pulseIsHigh()) {
                    return part1(remainingQueue, low, high)
                }
                destination.invertPulse()
            }
        }

        val additional = destination.targets.map { destination to it }
        return part1(remainingQueue + additional, low, high)
    }

    /**
     * Determine how many high and low pulses are sent across 1000 runs of the [modules]
     *
     * @return The product of the high and low pulse counts
     */
    fun part1(): Long {
        reset()
        return (0..<1000)
            .map { part1(initialTargets, 1, 0) }
            .reduce { acc, pair ->
                Pair(acc.first + pair.first, acc.second + pair.second)
            }
            .let { it.first * it.second }
    }

    /**
     * Instead of getting the number of high and low pulses, get the number of button presses needed to send a low
     * pulse to "rx".
     * We can, of course, determine the length of each cycle, then return the LCM of all the cycles
     *
     * @return The number of button presses
     */
    fun part2(): Long {
        reset()

        val finalModuleName = modules.first { "rx" in it.targets }.name
        val cycles = modules.filter { finalModuleName in it.targets }.associate { it.name to 0L }.toMutableMap()

        var count = 0L

        // Iterative version of the algorithm in part1 as opposed to doing it recursively
        val queue = ArrayDeque<Pair<Module, String>>()
        while (cycles.values.any { it == 0L }) {
            count += 1

            queue.addAll(initialTargets)
            while (queue.isNotEmpty()) {
                val (source, destName) = queue.removeFirst()

                // The destination is the module that updates "rx", and
                // The source pulse is high, then the memory would be set to high so there is a cycle on this loop
                // of modules
                if (destName == finalModuleName && source.pulseIsHigh()) {
                    cycles[source.name] = count
                }

                // Get the actual module we are targeting
                val destination = modules.firstOrNull { it.name == destName } ?: continue

                when (destination.moduleType) {
                    // Update the memory with of the Conjunction module with the source name and pulse. Then update the
                    // pulse of the Conjunction module
                    ModuleType.Conjunction -> destination.updateMemory(source)

                    // Continue on to the next, should not be reachable
                    ModuleType.Broadcast -> continue

                    // Continue on to the next if the pulse is already high, otherwise invert the pulse
                    ModuleType.FlipFlop -> {
                        if (source.pulseIsHigh()) {
                            continue
                        }
                        destination.invertPulse()
                    }
                }

                val additional = destination.targets.map { destination to it }
                queue.addAll(additional)
            }
        }

        return cycles.values.lcm()
    }

    companion object {
        fun parseInput(dataFile: DataFile): Propagation {
            val modules = fileToStream(2023, 20, dataFile).map { Module.fromString(it) }.toList()
            return Propagation(modules)
        }
    }
}


fun day20() {
    val input = Propagation.parseInput(DataFile.Part1)
    report(
        year = 2023,
        dayNumber = 20,
        part1 = input.part1(),
        part2 = input.part2(),
    )
}
