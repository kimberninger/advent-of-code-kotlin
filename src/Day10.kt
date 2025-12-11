import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPSolver

data class FactoryMachine(val targetConfiguration: Set<Int>, val buttons: List<Set<Int>>, val joltageRequirements: List<Int>) {
    companion object {
        val pattern = "\\[(?<target>[.#]+)] (?<buttons>\\(\\d+(?:,\\d+)*\\)(?: \\(\\d+(?:,\\d+)*\\))*) \\{(?<joltage>\\d+(?:,\\d+)*)}".toRegex()
        fun parse(manual: String) = pattern.matchEntire(manual)?.let { matchResult ->
            val target = matchResult.groups["target"]?.value
                ?.mapIndexedNotNull { index, symbol -> index.takeIf { symbol == '#' } }
                ?.toSet()
                ?: throw IllegalArgumentException("target configuration could not be parsed $manual")

            val buttons = matchResult.groups["buttons"]?.value
                ?.split(" ")
                ?.map { it.trim('(', ')').split(",").map { index -> index.toInt() }.toSet() }
                ?: throw IllegalArgumentException("buttons could not be parsed $manual")

            val joltage = matchResult.groups["joltage"]?.value
                ?.split(",")
                ?.map { it.toInt() }
                ?: throw IllegalArgumentException("joltage requirements could not be parsed $manual")

            FactoryMachine(target, buttons, joltage)
        } ?: throw IllegalArgumentException("target could not be parsed $manual")
    }
}

fun main() {
    Loader.loadNativeLibraries()

    fun <T> List<T>.powerSet(): Set<Set<T>> {
        if (this.isEmpty()) return setOf(emptySet())
        val first = this.first()
        val powerSetOfRest = this.drop(1).powerSet()
        return powerSetOfRest.map { it + first }.toSet() + powerSetOfRest
    }

    fun MPSolver.makeConstraint(exactValue: Double) = this.makeConstraint(exactValue, exactValue)

    fun requiredPressesForLights(machine: FactoryMachine) = machine.buttons.powerSet().filter { powerSet ->
        val resultingConfiguration = powerSet.fold(emptySet<Int>()) { state, buttons ->
            val toggleOn = buttons - state
            val toggleOff = state intersect buttons

            (state - toggleOff) union toggleOn
        }
        resultingConfiguration == machine.targetConfiguration
    }.minOf { it.size }

    fun requiredPressesForJoltage(machine: FactoryMachine): Int {
        val solver = MPSolver.createSolver("SCIP")
            ?: throw IllegalStateException("SCIP solver could not be created")

        val maxPresses = machine.joltageRequirements.max().toDouble()

        val variables = Array(machine.buttons.size) {
            solver.makeIntVar(0.0, maxPresses, "button-$it")
        }

        val objective = solver.objective().apply { setMinimization() }

        for ((index, requirement) in machine.joltageRequirements.withIndex()) {
            val constraint = solver.makeConstraint(requirement.toDouble())

            for ((button, variable) in machine.buttons zip variables) {
                if (index in button) {
                    constraint.setCoefficient(variable, 1.0)
                }

                objective.setCoefficient(variable, 1.0)
            }
        }

        return when (solver.solve()) {
            OPTIMAL, FEASIBLE -> variables.sumOf { it.solutionValue().toInt() }
            else -> throw IllegalStateException("no solution found")
        }
    }

    fun part1(input: List<String>) = input
        .map(FactoryMachine::parse)
        .sumOf(::requiredPressesForLights)

    fun part2(input: List<String>) = input
        .map(FactoryMachine::parse)
        .sumOf(::requiredPressesForJoltage)

    check(part1(readInput("Day10_test")) == 7)
    check(part2(readInput("Day10_test")) == 33)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
