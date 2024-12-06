package Solutions

import Common.AoCSolution

@Suppress("unused")
class Day_1() : AoCSolution() {
    override val day = "1"
    override fun FirstSolution() {
        val input = readInputAsString()
            .split("\n\n")
            .map {x -> x.split("\n")}
            .map {x -> x.filter {y -> y != ""}.map {z -> Integer.parseInt(z)}}
            .map {x -> x.sum()}
            .maxOrNull()
        print(input)
    }

    override fun SecondSolution() {
        val input = readInputAsString()
            .split("\n\n")
            .map {x -> x.split("\n")}
            .map {x -> x.filter {y -> y != ""}.map {z -> Integer.parseInt(z)}}
            .map {x -> x.sum()}
            .sortedBy { -it }
            .slice(0..2)
            .sum()
        print(input)
    }

}