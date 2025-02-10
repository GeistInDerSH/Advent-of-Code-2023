package com.geistindersh.aoc.year2019.intcomputer

class IntComputer(
    private val memory: MutableList<Long>,
    private val input: ArrayDeque<Long>,
) {
    private var instructionPointer = 0
    private var output: Long? = null
    private var relativeBaseOffset = 0L

    fun getOutput(): Long? {
        val out = output
        output = null
        return out
    }

    fun sendInput(value: Long) {
        input.add(value)
    }

    fun getMemory(): List<Long> = memory.toList()

    constructor(memory: List<Long>) : this(memory, emptyList())
    constructor(memory: List<Long>, input: List<Long>) : this(memory.toMutableList(), ArrayDeque<Long>(input))

    private fun getValueAtPosition(pos: Int): Long = if (pos >= memory.size) 0 else memory[pos]

    private fun getValueAtPosition(pos: Long) = getValueAtPosition(pos.toInt())

    private fun storeValueAtPosition(
        pos: Int,
        value: Long,
    ) {
        if (pos >= memory.size) {
            val count = pos - memory.size + 1
            memory.addAll(List(count) { 0 })
        }
        memory[pos] = value
    }

    private fun storeValueAtPosition(
        pos: Long,
        value: Long,
    ) = storeValueAtPosition(pos.toInt(), value)

    private fun storeValue(
        param: Parameter,
        value: Long,
    ) {
        when (param) {
            is Parameter.Position -> storeValueAtPosition(param.value, value)
            is Parameter.Relative -> storeValueAtPosition(param.value + relativeBaseOffset, value)
            is Parameter.Immediate -> throw Exception("Immediate value not supported for storing")
        }
    }

    private fun getWord(): Long = getValueAtPosition(instructionPointer).also { instructionPointer++ }

    private fun fetchParameter(mode: Long): Parameter {
        val word = getWord()
        return when (mode) {
            0L -> Parameter.Position(word)
            1L -> Parameter.Immediate(word)
            2L -> Parameter.Relative(word)
            else -> throw IllegalArgumentException("Unknown mode: $mode")
        }
    }

    private fun decodeParameter(param: Parameter): Long =
        when (param) {
            is Parameter.Immediate -> param.value
            is Parameter.Position -> getValueAtPosition(param.value)
            is Parameter.Relative -> getValueAtPosition(param.value + relativeBaseOffset)
        }

    private fun getSingleParameter(opcode: Long): Parameter = fetchParameter(opcode % 10)

    private fun getDoubleParameter(opcode: Long): Pair<Parameter, Parameter> {
        var opcode = opcode
        val p1 = getSingleParameter(opcode)
        opcode /= 10
        val p2 = getSingleParameter(opcode)
        return p1 to p2
    }

    private fun getTripleParameter(opcode: Long): Triple<Parameter, Parameter, Parameter> {
        val (p1, p2) = getDoubleParameter(opcode)
        var opcode = opcode / 100
        val p3 = getSingleParameter(opcode % 10)
        return Triple(p1, p2, p3)
    }

    private fun getNextInstruction(): Instruction {
        val word = getWord()
        val instr = word % 100
        val opcode = word / 100
        return when (instr) {
            1L -> Instruction.Add(getTripleParameter(opcode))
            2L -> Instruction.Mul(getTripleParameter(opcode))
            3L -> Instruction.Load(getSingleParameter(opcode))
            4L -> Instruction.Store(getSingleParameter(opcode))
            5L -> Instruction.JumpIfTrue(getDoubleParameter(opcode))
            6L -> Instruction.JumpIfFalse(getDoubleParameter(opcode))
            7L -> Instruction.LessThan(getTripleParameter(opcode))
            8L -> Instruction.Equals(getTripleParameter(opcode))
            9L -> Instruction.AdjustRelativeBase(getSingleParameter(opcode))
            99L -> Instruction.Halt
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
                storeValue(dest, arg1 + arg2)
                Signal.None
            }
            is Instruction.Mul -> {
                val arg1 = decodeParameter(instr.params.first)
                val arg2 = decodeParameter(instr.params.second)
                val dest = instr.params.third
                storeValue(dest, arg1 * arg2)
                Signal.None
            }
            is Instruction.Load -> {
                if (input.isEmpty()) {
                    instructionPointer -= 2
                    Signal.NeedsInput
                } else {
                    val i = input.removeFirst()
                    storeValue(instr.dest, i)
                    Signal.None
                }
            }
            is Instruction.Store -> {
                output = decodeParameter(instr.param)
                Signal.HasOutput
            }
            is Instruction.JumpIfTrue -> {
                val arg1 = decodeParameter(instr.params.first)
                if (arg1 != 0L) {
                    instructionPointer = decodeParameter(instr.params.second).toInt()
                }
                Signal.None
            }
            is Instruction.JumpIfFalse -> {
                val arg1 = decodeParameter(instr.params.first)
                if (arg1 == 0L) {
                    instructionPointer = decodeParameter(instr.params.second).toInt()
                }
                Signal.None
            }
            is Instruction.LessThan -> {
                val arg1 = decodeParameter(instr.params.first)
                val arg2 = decodeParameter(instr.params.second)
                val dest = instr.params.third
                val value = if (arg1 < arg2) 1L else 0L
                storeValue(dest, value)
                Signal.None
            }
            is Instruction.Equals -> {
                val arg1 = decodeParameter(instr.params.first)
                val arg2 = decodeParameter(instr.params.second)
                val dest = instr.params.third
                val value = if (arg1 == arg2) 1L else 0L
                storeValue(dest, value)
                Signal.None
            }
            is Instruction.AdjustRelativeBase -> {
                relativeBaseOffset += decodeParameter(instr.param)
                Signal.None
            }
            Instruction.Halt -> Signal.Halt
        }
    }

    fun runWhileSignal(signal: Signal): Signal {
        var sig = step()
        while (sig == signal) {
            sig = step()
        }
        return sig
    }

    fun runUntilSignal(signal: Signal): IntComputer {
        var sig = step()
        while (sig != signal) {
            sig = step()
        }
        return this
    }

    fun run(): IntComputer {
        runUntilSignal(Signal.Halt)
        return this
    }
}
