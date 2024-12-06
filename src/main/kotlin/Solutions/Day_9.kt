package Solutions

import Common.AoCSolution
import java.lang.Math.abs

@Suppress("unused")
class Day_9: AoCSolution() {
    override val day = "9"

    override fun FirstSolution() {
        val input = readInputAsListOfLines()
        var headPosition = Position(0,0)
        var tailPosition = Position(0,0)
        var tailVisited = TailVisited(mutableSetOf("0,0"))
        for(l in input) {
            processInstruction(l, headPosition, tailPosition, tailVisited)
        }
        println(tailVisited.visited.size)
    }

    override fun SecondSolution() {
        val input = readInputAsListOfLines()
        var headPosition = Position(0,0)
        var tailPositions = List<Position>(9) { Position(0,0) }
        var tailVisited = TailVisited(mutableSetOf("0,0"))
        for(l in input) {
            processInstructionPartTwo(l, headPosition, tailPositions, tailVisited)
        }
        println(tailVisited.visited.size)
    }

    data class Position(var x: Int, var y: Int) {
        override fun toString(): String {
            return "${x},${y}"
        }
    }

    data class TailVisited(val visited: MutableSet<String>)

    fun processInstruction(instruction: String, headPosition: Position, tailPosition: Position, tailVisited: TailVisited) {
        val (direction, steps) = instruction.split(" ")
        for(i in 1..Integer.parseInt(steps)) {
            processHeadInstruction(headPosition, direction)
            processTailInstruction(headPosition, tailPosition, tailVisited)
        }
    }

    fun processHeadInstruction(headPosition:Position, direction: String) {
        when(direction) {
            "R" -> headPosition.x += 1
            "L" -> headPosition.x -= 1
            "U" -> headPosition.y += 1
            "D" -> headPosition.y -= 1
        }
    }

    fun processTailInstruction(headPosition: Position, tailPosition: Position, tailVisited: TailVisited, isLast:Boolean = true) {
        if(abs(headPosition.x - tailPosition.x) + abs(headPosition.y - tailPosition.y) > 2) {
            if(headPosition.x - tailPosition.x > 0) tailPosition.x += 1 else tailPosition.x -= 1
            if(headPosition.y - tailPosition.y > 0) tailPosition.y += 1 else tailPosition.y -= 1
        }
        else if(abs(headPosition.x - tailPosition.x) > 1) {
            if(headPosition.x - tailPosition.x > 1) tailPosition.x += 1 else tailPosition.x -= 1
        }
        else if(abs(headPosition.y - tailPosition.y) > 1) {
            if(headPosition.y - tailPosition.y > 1) tailPosition.y += 1 else tailPosition.y -= 1
        }
        if(isLast) tailVisited.visited.add("${tailPosition}")
    }

    fun processInstructionPartTwo(instruction: String, headPosition: Position, tailPositions: List<Position>, tailVisited: TailVisited) {
        val (direction, steps) = instruction.split(" ")
        for(i in 1..Integer.parseInt(steps)) {
            processHeadInstruction(headPosition, direction)
            processTailInstruction(headPosition, tailPositions[0], tailVisited, false)
            for(i in 1..7) {
                processTailInstruction(tailPositions[i-1], tailPositions[i], tailVisited, false)
            }
            processTailInstruction(tailPositions[7], tailPositions[8], tailVisited, true)
        }
    }
}