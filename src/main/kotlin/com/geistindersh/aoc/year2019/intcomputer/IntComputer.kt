package com.geistindersh.aoc.year2019.intcomputer

class IntComputer(
    private val memory: MutableList<Int>,
    private val input: ArrayDeque<Int>,
) {
    private var instructionPointer = 0
    private var output: Int? = null

    fun getOutput(): Int? {
        val out = output
        output = null
        return out
    }

    constructor(memory: List<Int>) : this(memory, emptyList())
    constructor(memory: List<Int>, input: List<Int>) : this(memory.toMutableList(), ArrayDeque<Int>(input))

    private fun getValueAtPosition(pos: Int): Int = if (pos >= memory.size) 0 else memory[pos]

    private fun storeValueAtPosition(
        pos: Int,
        value: Int,
    ) {
        if (pos >= memory.size) {
            val count = memory.size - pos
            memory.addAll(List(count) { 0 })
        }
        memory[pos] = value
    }

    private fun getWord(): Int = getValueAtPosition(instructionPointer).also { instructionPointer++ }

    private fun fetchParameter(mode: Int): Parameter {
        val word = getWord()
        return when (mode) {
            0 -> Parameter.Position(word)
            1 -> Parameter.Immediate(word)
            else -> throw IllegalArgumentException("Unknown mode: $mode")
        }
    }

    private fun decodeParameter(param: Parameter) =
        when (param) {
            is Parameter.Immediate -> param.value
            is Parameter.Position -> getValueAtPosition(param.value)
        }

    private fun getSingleParameter(opcode: Int): Parameter = fetchParameter(opcode % 10)

    private fun getTripleParameter(opcode: Int): Triple<Parameter, Parameter, Parameter> {
        var opcode = opcode
        val p1 = getSingleParameter(opcode)
        opcode /= 10
        val p2 = getSingleParameter(opcode)
        opcode /= 10
        val p3 = getSingleParameter(opcode % 10)
        return Triple(p1, p2, p3)
    }

    private fun getNextInstruction(): Instruction {
        val word = getWord()
        val instr = word % 100
        val opcode = word / 100
        return when (instr) {
            1 -> Instruction.Add(getTripleParameter(opcode))
            2 -> Instruction.Mul(getTripleParameter(opcode))
            3 -> Instruction.Load(getSingleParameter(opcode))
            4 -> Instruction.Store(getSingleParameter(opcode))
            99 -> Instruction.Halt
            else -> throw IllegalArgumentException("Invalid op $instr")
        }
    }

    private fun step(): Signal {
        val instr = getNextInstruction()
        return when (instr) {
            is Instruction.Add -> {
                val arg1 = decodeParameter(instr.params.first)
                val arg2 = decodeParameter(instr.params.second)
                val dest = instr.params.third
                storeValueAtPosition(dest.value, arg1 + arg2)
                Signal.None
            }
            is Instruction.Mul -> {
                val arg1 = decodeParameter(instr.params.first)
                val arg2 = decodeParameter(instr.params.second)
                val dest = instr.params.third
                storeValueAtPosition(dest.value, arg1 * arg2)
                Signal.None
            }
            is Instruction.Load -> {
                val i = input.removeFirst()
                storeValueAtPosition(instr.dest.value, i)
                Signal.None
            }
            is Instruction.Store -> {
                output = decodeParameter(instr.param)
                Signal.HasOutput
            }
            Instruction.Halt -> Signal.Halt
        }
    }

    fun runUntilSignal(signal: Signal) {
        var sig = step()
        while (sig != signal) {
            sig = step()
        }
    }

    fun run() = runUntilSignal(Signal.Halt)
}
