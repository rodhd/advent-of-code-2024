import Common.SolutionFactory.generateSolution

fun main(args: Array<String>) {
    println("Please enter the day:")
    val day = readln()
    val solution = generateSolution(day)
    if(solution != null) {
        println("\nFirst response:")
        solution.FirstSolution()
        println("\nSecond response:")
        solution.SecondSolution()
    }
    else {
        println("Invalid Day")
    }


}