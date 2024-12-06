package Solutions

import Common.AoCSolution

@Suppress("unused")
class Day_8: AoCSolution() {
    override val day = "8"

    override fun FirstSolution() {
        val treeMap = readInputAsListOfLines()
            .map{x -> x.toCharArray()
                .map{y -> y.digitToInt()}}

        var result = 0

        for(i in 0..treeMap.lastIndex) {
            for(j in 0..treeMap[i].lastIndex) {
                val treeLines = getTreeHeights(i,j,treeMap)
                if(isVisible(treeMap[i][j], treeLines)) {
                    result += 1
                }
            }
        }
        println(result)
    }

    override fun SecondSolution() {
        val treeMap = readInputAsListOfLines()
            .map{x -> x.toCharArray()
                .map{y -> y.digitToInt()}}

        var result = 0

        for(i in 0..treeMap.lastIndex) {
            for(j in 0..treeMap[i].lastIndex) {
                val treeLines = getTreeHeights(i,j,treeMap)
                val currentScenicScore = getScenicScore(treeMap[i][j], treeLines)
                if(currentScenicScore > result) result = currentScenicScore
            }
        }
        println(result)
    }

    data class TreeLines(val top: List<Int>, val bottom: List<Int>, val left: List<Int>, val right: List<Int>)

    fun getTreeHeights(i: Int, j: Int, trees: List<List<Int>>): TreeLines {
        val top: List<Int> = trees.slice(0..i-1).map{x -> x[j]}.reversed()
        val bottom: List<Int> = trees.slice(i+1..trees.lastIndex).map{x -> x[j]}
        val left: List<Int> = trees[i].slice(0..j-1).reversed()
        val right: List<Int> = trees[i].slice(j+1..trees[i].lastIndex)
        return TreeLines(top, bottom, left, right)
    }
    fun isVisible(currentTree: Int, treeLines: TreeLines): Boolean {
        return treeLines.top.all { x -> x < currentTree }
                || treeLines.bottom.all { x -> x < currentTree }
                || treeLines.left.all { x -> x < currentTree }
                || treeLines.right.all { x -> x < currentTree }

    }

    fun visibleTrees(currentTree: Int, lineOfTrees: List<Int>): Int {
        if(lineOfTrees.isEmpty()) {
            return 0
        }
        val indexOfNextVisibleTree = lineOfTrees.indexOfFirst { x -> x >= currentTree }
        if(indexOfNextVisibleTree == -1) {
            return lineOfTrees.size
        }
        return lineOfTrees.indexOfFirst { x -> x >= currentTree } + 1
    }

    fun getScenicScore(currentTree: Int, treeLines: TreeLines): Int {
        return visibleTrees(currentTree, treeLines.top) *
                visibleTrees(currentTree, treeLines.bottom) *
                visibleTrees(currentTree, treeLines.left)*
                visibleTrees(currentTree, treeLines.right)
    }
}