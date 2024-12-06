package Solutions

import Common.AoCSolution

@Suppress("unused")
class Day_5: AoCSolution() {
    override val day = "5"

    val crateIndexes = listOf<Int>(1,5,9,13,17,21,25,29,33)
    val instructionRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")

    override fun FirstSolution() {
        val input = readInputAsListOfLines()
        var crates = getCrateStacks(input.slice(0..7))
        val instructions = input
            .slice(10.. input.lastIndex)
            .map {x -> parseInstructions(x)}

        for (i in instructions) {
            crates = processInstructions(crates, i!!)
        }
        val result = crates.map {x -> x.last()}
        println(result)
    }

    override fun SecondSolution() {
        val input = readInputAsListOfLines()
        var crates = getCrateStacks(input.slice(0..7))
        val instructions = input
            .slice(10.. input.lastIndex)
            .map {x -> parseInstructions(x)}

        for (i in instructions) {
            crates = processInstructionsUpgrade(crates, i!!)
        }
        val result = crates.map {x -> x.last()}
        println(result)
    }

    fun getCrateStacks(crates: List<String>): List<List<Char>> {
        var parsedCrates =  MutableList(9) { mutableListOf<Char>() }
        for(i in crates.lastIndex downTo 0) {
            for(c in 0..crateIndexes.lastIndex) {
                val el = crates[i][crateIndexes[c]]
                if (el != ' ') {
                    parsedCrates[c].add(crates[i][crateIndexes[c]])
                }

            }
        }
        return parsedCrates
    }

    fun parseInstructions(inst: String): Triple<Int, Int, Int>? {
        val result = instructionRegex.matchEntire(inst)?.groupValues
        if (result != null) {
            return Triple(Integer.parseInt(result[1]), Integer.parseInt(result[2]), Integer.parseInt(result[3]))
        }
        return null
    }

    fun processInstructions(crates: List<List<Char>>, instructions: Triple<Int, Int, Int>): List<List<Char>> {
        var cratesCopy = crates
            .toMutableList()
            .map{x -> x.toMutableList()}
        for(i in 0 until instructions.first) {
            cratesCopy[instructions.third -1].add(cratesCopy[instructions.second - 1].last())
            cratesCopy[instructions.second - 1].removeLast()
        }
        return cratesCopy
    }

    fun processInstructionsUpgrade(crates: List<List<Char>>, instructions: Triple<Int, Int, Int>): List<List<Char>> {
        var cratesCopy = crates
            .toMutableList()
            .map{x -> x.toMutableList()}
        cratesCopy[instructions.third -1].addAll(cratesCopy[instructions.second - 1].takeLast(instructions.first)
        )
        repeat(instructions.first) {
            cratesCopy[instructions.second - 1].removeLast()
        }
        return cratesCopy
    }
}