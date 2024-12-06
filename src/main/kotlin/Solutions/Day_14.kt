package Solutions

import Common.AoCSolution
import java.security.InvalidParameterException

class Day_14: AoCSolution() {
    override val day = "14"

    override fun FirstSolution() {
        var input = readInputAsListOfLines().map{x -> parseRockLine(x)}
        val caveMap = buildCaveMap(input)
        val result = start(caveMap)
        println(result)
    }

    override fun SecondSolution() {
        var input = readInputAsListOfLines().map{x -> parseRockLine(x)}
        val caveMap = buildCaveMap(input)
        val result = startWithFloor(caveMap)
        println(result)
    }

    fun parseRockLine(text: String): List<Pair<Int,Int>> {
        return text.split(" -> ").map{x -> Pair(x.split(",")[0]!!.toInt(), x.split(",")[1]!!.toInt())}
    }

    enum class MapTyle {
        AIR, ROCK, SAND, SANDSOURCE
    }

    data class CaveMap(val tyles: MutableMap<Pair<Int,Int>, MapTyle>) {
        operator fun get(location: Pair<Int, Int>) = tyles[location]

        var floorHeight = 0

        fun getTyle(location: Pair<Int, Int>): MapTyle {
            if(tyles.containsKey(location) && tyles[location] !=  null) {
                return tyles[location]!!
            }
            else {
                return MapTyle.AIR
            }
        }

        fun getTyleWithFloor(location: Pair<Int, Int>): MapTyle {
            if(location.second == floorHeight) {
                return MapTyle.ROCK
            } else {
                return getTyle(location)
            }
        }

        operator fun set(location: Pair<Int, Int>, value: MapTyle) {
            tyles[location] = value
        }
    }

    fun buildCaveMap(rockLines: List<List<Pair<Int, Int>>>): CaveMap {
        val result = CaveMap(mutableMapOf(Pair(500,0) to MapTyle.SANDSOURCE))
        for(l in rockLines) {
            for(i in 0 until l.lastIndex) {
                if(l[i].first == l[i+1].first) {
                    if(l[i].second < l[i+1].second) {
                        for(j in l[i].second .. l[i+1].second) {
                            result[Pair(l[i].first,j)] = MapTyle.ROCK
                        }
                    }
                    else {
                        for(j in l[i+1].second .. l[i].second) {
                            result[Pair(l[i].first,j)] = MapTyle.ROCK
                        }
                    }
                } else if(l[i].second == l[i+1].second) {
                    if(l[i].first < l[i+1].first) {
                        for(j in l[i].first .. l[i+1].first) {
                            result[Pair(j,l[i].second)] = MapTyle.ROCK
                        }
                    }
                    else {
                        for(j in l[i+1].first .. l[i].first) {
                            result[Pair(j,l[i].second)] = MapTyle.ROCK
                        }
                    }
                }
                else {
                    throw InvalidParameterException()
                }
            }
        }
        result.floorHeight = result.tyles.keys.maxBy { it.second }.second + 2
        return result
    }

    fun start(caveMap: CaveMap): Int {
        var counter = 0
        var reachedAbyss = false
        while(!reachedAbyss) {
            reachedAbyss = addSandUnit(caveMap)
            if(!reachedAbyss) counter += 1
        }
        return counter
    }

    fun addSandUnit(caveMap: CaveMap): Boolean {
        var currentPosition = Pair(500,0)
        var nextPosition = getNextPosition(currentPosition, caveMap)
        val y_max = caveMap.tyles.keys.maxBy { it.second }.second
        while(currentPosition != nextPosition) {
            currentPosition = nextPosition
            nextPosition = getNextPosition(currentPosition, caveMap)
            if(nextPosition.second > y_max) {
                return true
            }
        }
        caveMap[currentPosition] = MapTyle.SAND
        return false
    }

    fun getNextPosition(currentPosition: Pair<Int, Int>, caveMap: CaveMap): Pair<Int, Int> {
        val (x, y) = currentPosition
        if(caveMap.getTyle(Pair(x, y+1)) == MapTyle.AIR) {
            return Pair(x, y + 1)
        }
        if(caveMap.getTyle(Pair(x-1, y+1)) == MapTyle.AIR) {
            return Pair(x-1, y + 1)
        }
        if(caveMap.getTyle(Pair(x+1, y+1)) == MapTyle.AIR) {
            return Pair(x+1, y + 1)
        }
        return currentPosition
    }
    fun startWithFloor(caveMap: CaveMap): Int {
        var counter = 0
        var reachedFloor = false
        while(!reachedFloor) {
            reachedFloor = addSandUnitWithFloor(caveMap)
            if(!reachedFloor) counter += 1
        }
        return counter
    }

    fun addSandUnitWithFloor(caveMap: CaveMap): Boolean {
        var currentPosition = Pair(500,0)
        var nextPosition = getNextPositionWithFloor(currentPosition, caveMap)
        while(currentPosition != nextPosition) {
            currentPosition = nextPosition
            nextPosition = getNextPositionWithFloor(currentPosition, caveMap)
        }
        caveMap[currentPosition] = MapTyle.SAND
        if(currentPosition == Pair(500,0)) {
            return true
        }
        return false
    }

    fun getNextPositionWithFloor(currentPosition: Pair<Int, Int>, caveMap: CaveMap): Pair<Int, Int> {
        val (x, y) = currentPosition
        if(caveMap.getTyleWithFloor(Pair(x, y+1)) == MapTyle.AIR) {
            return Pair(x, y + 1)
        }
        if(caveMap.getTyleWithFloor(Pair(x-1, y+1)) == MapTyle.AIR) {
            return Pair(x-1, y + 1)
        }
        if(caveMap.getTyleWithFloor(Pair(x+1, y+1)) == MapTyle.AIR) {
            return Pair(x+1, y + 1)
        }
        return currentPosition
    }
}