package Solutions

import Common.AoCSolution
import java.lang.IllegalArgumentException

@Suppress("unused")
class Day_2() : AoCSolution() {
    override val day = "2"
    override fun FirstSolution() {
        val value = readInputAsListOfLines()
            .map {x -> calculateRoundScore(x)}
            .sum()
        println(value)
    }

    override fun SecondSolution() {
        val value = readInputAsListOfLines()
            .map {x -> calculateRoundScoreAlternate(x)}
            .sum()
        println(value)
    }
    fun calculateRoundScore(roundStrategy: String): Int {
        return when (roundStrategy) {
            "A X" -> 1 + 3
            "A Y" -> 2 + 6
            "A Z" -> 3 + 0
            "B X" -> 1 + 0
            "B Y" -> 2 + 3
            "B Z" -> 3 + 6
            "C X" -> 1 + 6
            "C Y" -> 2 + 0
            "C Z" -> 3 + 3
            else -> throw IllegalArgumentException("Wrong input")
        }
    }

    fun calculateRoundScoreAlternate(roundStrategy: String): Int {
        return when (roundStrategy) {
            "A X" -> 3 + 0
            "A Y" -> 1 + 3
            "A Z" -> 2 + 6
            "B X" -> 1 + 0
            "B Y" -> 2 + 3
            "B Z" -> 3 + 6
            "C X" -> 2 + 0
            "C Y" -> 3 + 3
            "C Z" -> 1 + 6
            else -> throw IllegalArgumentException("Wrong input")
        }
    }
}