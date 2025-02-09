package com.geistindersh.aoc.year2019.intcomputer

sealed class Instruction {
    data class Add(
        val params: Triple<Parameter, Parameter, Parameter>,
    ) : Instruction()

    data class Mul(
        val params: Triple<Parameter, Parameter, Parameter>,
    ) : Instruction()

    data class Load(
        val dest: Parameter,
    ) : Instruction()

    data class Store(
        val param: Parameter,
    ) : Instruction()

    object Halt : Instruction()
}
