package Solutions

import Common.AoCSolution

class Day_11: AoCSolution() {
    override val day = "11"

    override fun FirstSolution() {
        val input = readInputAsString().split("\n\n")
        val game = MonkeyGame(input)
        game.playNRound(20)
        println(game.getMonkeyBusiness())
    }

    override fun SecondSolution() {
        val input = readInputAsString().split("\n\n")
        val game = MonkeyGame(input, 1u)
        game.playNRound(10000)
        println(game.getMonkeyBusiness())
    }

    class MonkeyGame(descriptions: List<String>, val worryDivider: ULong = 3u) {
        var monkeys: List<Monkey> = descriptions.map{x -> Monkey(x)}
        var round = 0
        val gcm = monkeys.map{x -> x.divisibleBy}.reduce{a,b -> a * b}


        fun playNRound(n: Int) {
            repeat(n) {
                playRound()
            }
        }
        fun playRound() {
            if(round % 1000 == 0) {
                println("Round: ${round}")
            }
            for(m in monkeys) {
                inspectItem(m, monkeys)
            }
            round += 1
        }

        fun inspectItem(activeMonkey: Monkey, monkeys: List<Monkey>) {
            while(activeMonkey.items.isNotEmpty()) {
                val nextItem = activeMonkey.items.removeFirst()
                activeMonkey.inspections += 1
                val newWorryLevel = when(activeMonkey.operationNew) {
                    "+" -> (nextItem + (activeMonkey.operationFactor ?: nextItem)) % gcm
                    "-" -> (nextItem - (activeMonkey.operationFactor ?: nextItem)) % gcm
                    "*" -> (nextItem * (activeMonkey.operationFactor ?: nextItem)) % gcm
                    else -> {throw IllegalArgumentException()}
                }
                if(newWorryLevel % activeMonkey.divisibleBy == "0".toULong()) {
                    monkeys.find{x -> x.monkeyNumber == activeMonkey.monkeyIfTrue}?.items?.add(newWorryLevel)
                } else {
                    monkeys.find{x -> x.monkeyNumber == activeMonkey.monkeyIfFalse}?.items?.add(newWorryLevel)
                }
            }
        }

        fun getMonkeyBusiness(): Long {
            return monkeys.sortedBy { -it.inspections }.slice(0..1).map{x -> x.inspections}.reduce() {a, b -> a * b}
        }
    }

    class Monkey(description: String) {
        val monkeyRegex = Regex("Monkey (\\d+):\\n\\s\\sStarting items: ([\\d\\s\\,]+)\\n\\s\\sOperation: new = old ([\\+\\-\\*])\\s(\\d+|old)\\n\\s\\sTest: divisible by (\\d+)\\n\\s\\s\\s\\sIf true: throw to monkey (\\d+)\\n\\s\\s\\s\\sIf false: throw to monkey (\\d+)").matchEntire(description)?.groupValues

        val monkeyNumber: Int = Integer.parseInt(monkeyRegex?.get(1))

        val items: MutableList<ULong> = (monkeyRegex?.get(2)
            ?.split(", ")?.map{ x -> x.toULong()} ?: mutableListOf<ULong>()).toMutableList()

        val operationNew: String = monkeyRegex?.get(3) ?: ""

        val operationFactor: ULong? = if(monkeyRegex?.get(4) == "old") null else (monkeyRegex?.get(4))?.toULong()

        val divisibleBy: ULong = monkeyRegex?.get(5)!!.toULong()

        val monkeyIfTrue: Int = Integer.parseInt(monkeyRegex?.get(6))

        val monkeyIfFalse: Int = Integer.parseInt(monkeyRegex?.get(7))

        var inspections: Long = 0
    }
}

