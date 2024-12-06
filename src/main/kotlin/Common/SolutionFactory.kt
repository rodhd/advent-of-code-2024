package Common
object SolutionFactory {

    fun generateSolution(day: String): AoCSolution {
        return Class.forName("Solutions.Day_$day").getConstructor().newInstance() as AoCSolution
    }
}