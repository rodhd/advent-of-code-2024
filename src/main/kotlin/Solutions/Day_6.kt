package Solutions

import Common.AoCSolution

@Suppress("unused")
class Day_6: AoCSolution() {
    override val day = "6"

    override fun FirstSolution() {
        val buffer = readInputAsString()
        val result = findStartMarker(buffer)
        println(result)
    }

    override fun SecondSolution() {
        val buffer = readInputAsString()
        val result = findMessageMarker(buffer)
        println(result)
    }

    private fun isStartMarker(marker: String): Boolean {
        return (marker[0] != marker[1]) &&
                (marker[0] != marker[2]) &&
                (marker[0] != marker[3]) &&
                (marker[1] != marker[2]) &&
                (marker[1] != marker[3]) &&
                (marker[2] != marker[3])
    }

    private fun findStartMarker(buffer: String): Int {
        for(i in 3..buffer.lastIndex) {
           if(isStartMarker(buffer.slice(i-3..i))) {
               return i + 1
           }
        }
        return -1
    }

    private fun isMessageMarker(marker: String): Boolean {
        val charList = marker.toList()
        return !charList.groupingBy { it }.eachCount().any{x -> x.value > 1}
    }

    private fun findMessageMarker(buffer: String): Int {
        for(i in 13..buffer.lastIndex) {
            if(isMessageMarker(buffer.slice(i-13..i))) {
                return i + 1
            }
        }
        return -1
    }

}