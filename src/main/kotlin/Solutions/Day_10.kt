package Solutions

import Common.AoCSolution
import kotlin.math.floor

@Suppress("unused")
class Day_10: AoCSolution() {
    override val day = "10"

    override fun FirstSolution() {
        val input = readInputAsListOfLines()
        var registerState = RegisterState(mutableMapOf(0 to 1), 1)
        for(l in input) {
            processCommand(l, registerState)
        }
        println(getSignalStrengths(registerState))
    }

    override fun SecondSolution() {
        val input = readInputAsListOfLines()
        var registerState = RegisterState(mutableMapOf(0 to 1), 1)
        for(l in input) {
            processCommand(l, registerState)
        }
        var crtDisplay = CRTDisplay(MutableList(6) {MutableList(40) {"."} })
        drawPixels(crtDisplay, registerState)
        crtDisplay.printScreen()
    }

    data class RegisterState(var stateHistory: MutableMap<Int, Int>, var register: Int) {
        fun getLastCycleNumber(): Int {
            return this.stateHistory.keys.max()
        }
    }

    fun processCommand(command: String, registerState: RegisterState) {
        if(command == "noop") {
            noop(registerState)
        } else {
            val value = command.split(" ")[1]
            addx(Integer.parseInt(value!!), registerState)
        }
    }

    fun addx(value: Int, registerState: RegisterState) {
        val lastCycle = registerState.getLastCycleNumber()
        for(i in lastCycle + 1..lastCycle + 2) {
            registerState.stateHistory[i] = registerState.register
        }
        registerState.register += value
    }

    fun noop(registerState: RegisterState) {
        val lastCycle = registerState.getLastCycleNumber()
        registerState.stateHistory[lastCycle + 1] = registerState.register
    }

    fun getSignalStrengths(registerState: RegisterState): Int {
        val cycleList = listOf(20,60,100,140,180,220)
        return registerState
            .stateHistory
            .filterKeys { k -> cycleList.contains(k) }
            .map{ x -> x.key * x.value}
            .sum()
    }

    data class CRTDisplay(var value: MutableList<MutableList<String>>) {
        fun printScreen() {
            for(l in value) {
                println(l.reduce(){ a, b -> a + b})
            }
        }

        fun updateDisplay(cycle: Int) {
            val v = cycle / 40
            val h = if(cycle % 40 == 0) 39 else cycle - v * 40 - 1
            value[v][h] = "#"
        }
    }

    fun drawPixels(crtDisplay: CRTDisplay, registerState: RegisterState) {
        for((cycle, register) in registerState.stateHistory) {
            if(cycle == 0) {
                continue
            }
            else if(cycle % 40 == 0) {
                if(cycle % 40 in register - 2..register) {
                    crtDisplay.updateDisplay(cycle)
                }
            }
            else if(cycle % 40 == 1) {
                if(cycle % 40 in register..register + 2) {
                    crtDisplay.updateDisplay(cycle)
                }
            }
            else if(cycle % 40 - 1 in register - 1..register + 1) {
                crtDisplay.updateDisplay(cycle)
            }
        }
    }
}