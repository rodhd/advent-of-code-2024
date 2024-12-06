package Solutions

import Common.AoCSolution

@Suppress("unused")
class Day_3(): AoCSolution() {
    override val day = "3"
    override fun FirstSolution() {
        val r = readInputAsListOfLines()
            .map {x -> findCommonItemPriority(x)}
            .sum()
        println(r)
    }

    override fun SecondSolution() {
        println(findBadgePriorities(readInputAsListOfLines()))
    }

    fun findCommonItemPriority(rucksack: String): Int {
        val (firstHalf, secondHalf) = Pair(
            rucksack.toCharArray().slice(0..rucksack.length/2 - 1).toSet(),
            rucksack.toCharArray().slice(rucksack.length/2 .. rucksack.length - 1).toSet())

        val commonItem = firstHalf.intersect(secondHalf).first().code
        return convertToPriority(commonItem)
    }

    private fun convertToPriority(commonItem: Int): Int {
        if (commonItem >= 97) {
            return commonItem - 96
        } else {
            return commonItem - 64 + 26
        }
    }

    fun findBadgePriorities(rucksacks: List<String>): Int {
        var score = 0
        for(i in 0..rucksacks.lastIndex step 3) {
            val group = rucksacks.slice(i..i+2).map {x -> x.toCharArray().toList()}
            val commonItem = group[0].intersect(group[1]).intersect(group[2]).first().code
            score += convertToPriority(commonItem)
        }
        return score
    }
}