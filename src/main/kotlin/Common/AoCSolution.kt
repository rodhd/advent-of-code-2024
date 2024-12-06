package Common

abstract class AoCSolution() {

    abstract val day: String
    abstract fun FirstSolution()

    abstract fun SecondSolution()

    fun readInputAsString(): String {
        val fileName = "/inputs/input-$day.txt"
        return this.javaClass.getResource(fileName)?.readText(Charsets.UTF_8) ?: "not found"
    }

    fun readInputAsListOfLines(): List<String>
        = this.javaClass.getResourceAsStream("/inputs/input-$day.txt")?.bufferedReader()?.readLines() ?: listOf("not used")
}