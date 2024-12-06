package Solutions

import Common.AoCSolution

class Day_12: AoCSolution() {
    override val day = "12"

    override fun FirstSolution() {
        val input = readInputAsListOfLines()
        val result = dijkstraAlgorithm(input)
        println(result)
    }

    override fun SecondSolution() {
        val input = readInputAsListOfLines()
        val result = reverseDijkstraAlgorithm(input)
        println(result)
    }

    fun dijkstraAlgorithm(input: List<String>): Int {
        val (startingPoint, endPoint, heightMap) = processMap(input)
        val vectors = getVectors(heightMap)
        var unvisitedNodes = vectors.map{x -> x.first}.toMutableSet()
        var visitedNodes: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var distances = initDistances(startingPoint, unvisitedNodes, vectors)

        var currentNode = startingPoint

        while(unvisitedNodes.isNotEmpty()) {
            val neighbors = vectors.filter { x -> x.first == currentNode }.map{ x -> x.second }.toSet()
            distances.putAll(neighbors.map{x -> x to 1 + distances.get(currentNode)!!})
            currentNode = distances.toList().filter{x -> !visitedNodes.contains(x.first)}.sortedBy { (_, value) -> value }.first().first
            unvisitedNodes.remove(currentNode)
            visitedNodes.add(currentNode)
        }

        return distances[endPoint]!!
    }

    fun reverseDijkstraAlgorithm(input: List<String>): Int {
        val (startingPoint, endPoint, heightMap) = processMap(input)
        val vectors = getVectors(heightMap).map{x -> Pair(x.second, x.first)}.toSet()
        var unvisitedNodes = vectors.map{x -> x.first}.toMutableSet()
        var visitedNodes: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var distances = initDistances(endPoint, unvisitedNodes, vectors)

        var currentNode = endPoint
        val lowestPoints = getLowestPoints(heightMap)
        var lowestPoint = Int.MAX_VALUE

        while(unvisitedNodes.isNotEmpty()) {
            val neighbors = vectors.filter { x -> x.first == currentNode }.map{ x -> x.second }.toSet()
            distances.putAll(neighbors.map{x -> x to 1 + distances.get(currentNode)!!})
            currentNode = distances.toList().filter{x -> !visitedNodes.contains(x.first)}.sortedBy { (_, value) -> value }.first().first
            if(lowestPoints.contains(currentNode) && distances[currentNode]!! < lowestPoint) {
                lowestPoint = distances[currentNode]!!
            }
            unvisitedNodes.remove(currentNode)
            visitedNodes.add(currentNode)
        }
        return lowestPoint
    }

    fun processMap(input: List<String>): Triple<Pair<Int,Int>, Pair<Int,Int>, List<List<Int>>> {
        val startingPoint = Pair<Int, Int>(input.indexOfFirst { x -> x.contains("S") }, input.find{x -> x.contains("S")}!!.indexOf("S"))
        val endingPoint = Pair<Int, Int>(input.indexOfFirst { x -> x.contains("E") }, input.find{x -> x.contains("E")}!!.indexOf("E"))
        val heights: List<List<Int>> = input.map{x -> x.map{y -> if(y == 'S') 'a'.code else if(y == 'E') 'z'.code else y.code}}
        return Triple(startingPoint, endingPoint, heights)
    }

    fun getLowestPoints(heightMap: List<List<Int>>): Set<Pair<Int,Int>> {
        var result = mutableSetOf<Pair<Int, Int>>()
        for(i in 0..heightMap.lastIndex) {
            for(j in 0..heightMap[i].lastIndex) {
                if(heightMap[i][j] == 'a'.code) {
                    result.add(Pair(i, j))
                }
            }
        }
        return result
    }

    fun getVectors(heights: List<List<Int>>): Set<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        var result: MutableSet<Pair<Pair<Int, Int>, Pair<Int, Int>>> = mutableSetOf()
        for(i in 0..heights.lastIndex) {
            for(j in 0..heights[i].lastIndex){
                if(heights.getOrNull(i-1)?.getOrNull(j) != null && heights[i-1][j] - heights[i][j] <= 1) result.add(Pair(Pair(i,j), Pair(i-1, j)))
                if(heights.getOrNull(i)?.getOrNull(j-1) != null && heights[i][j-1] - heights[i][j] <= 1) result.add(Pair(Pair(i,j), Pair(i, j-1)))
                if(heights.getOrNull(i+1)?.getOrNull(j) != null && heights[i+1][j] - heights[i][j] <= 1) result.add(Pair(Pair(i,j), Pair(i+1, j)))
                if(heights.getOrNull(i)?.getOrNull(j+1) != null && heights[i][j+1] - heights[i][j] <= 1) result.add(Pair(Pair(i,j), Pair(i, j+1)))
            }
        }
        return result
    }

    fun initDistances(node: Pair<Int, Int>, unvisitedNodes: Set<Pair<Int, Int>>, vectors: Set<Pair<Pair<Int, Int>, Pair<Int, Int>>>): MutableMap<Pair<Int, Int>, Int> {
        var distances: MutableMap<Pair<Int, Int>, Int> = mutableMapOf(node to 0)

        //distances.putAll(vectors.filter { x -> x.first == node }.map{x -> x.second to 1})

        distances.putAll(unvisitedNodes.filter { x -> !distances.keys.contains(x) }.map{x -> x to 1000000})

        return distances
    }
}