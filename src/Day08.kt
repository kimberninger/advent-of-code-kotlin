open class Node(val x: Long, val y: Long, val z: Long) {
    infix fun distanceTo(other: Node) =
        (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)

    companion object {
        fun parse(line: String): Node {
            val (x, y, z) = line.split(",").map { it.toLong() }
            return Node(x, y, z)
        }
    }
}

class IdentifiableNode(val id: Int, x: Long, y: Long, z: Long): Node(x, y, z) {
    constructor(id: Int, node: Node) : this(id, node.x, node.y, node.z)
}

data class Connection(val firstNode: IdentifiableNode, val secondNode: IdentifiableNode) {
    val distance: Long
        get() = firstNode distanceTo secondNode
}

class Circuit(nodeIds: Set<Int>? = null) {
    val nodeIds = nodeIds?.toMutableSet() ?: mutableSetOf()

    operator fun contains(connection: Connection) =
        connection.firstNode.id in nodeIds && connection.secondNode.id in nodeIds

    operator fun contains(nodeId: Int) = nodeId in nodeIds

    operator fun plusAssign(connection: Connection) {
        nodeIds += connection.firstNode.id
        nodeIds += connection.secondNode.id
    }

    val size
        get() = nodeIds.size
}

class Circuits {
    val circuits = mutableListOf<Circuit>()

    operator fun contains(connection: Connection) = circuits.any { connection in it }

    operator fun plusAssign(connection: Connection) {
        val connectedCircuits = circuits.filter { connection.firstNode.id in it || connection.secondNode.id in it}
        when (connectedCircuits.size) {
            0 -> circuits.add(Circuit().also { it += connection })
            1 -> connectedCircuits.first() += connection
            2 -> {
                circuits.removeAll(connectedCircuits)

                val combinedNodeIds = connectedCircuits.fold(emptySet<Int>()) {
                    acc, x -> acc.union(x.nodeIds)
                }

                val combinedCircuit = Circuit(combinedNodeIds)
                combinedCircuit += connection
                circuits.add(combinedCircuit)
            }
        }
    }

    val answer
        get() = circuits.map(Circuit::size).sortedDescending().take(3).product()
}

fun main() {
    fun part1(input: List<String>, connectionsToEstablish: Int): Int {
        val nodes = input
            .map(Node::parse)
            .withIndex()
            .map { (index, node) -> IdentifiableNode(index, node) }

        val connections = buildList {
            for (firstIndex in 0..<nodes.size) {
                for (secondIndex in firstIndex+1..<nodes.size) {
                    add(Connection(nodes[firstIndex], nodes[secondIndex]))
                }
            }
        }.sortedBy { it.distance }

        val circuits = Circuits()

        var establishedConnections = 0
        for (connection in connections) {
            if (connection !in circuits) {
                circuits += connection
            }
            establishedConnections++
            if (establishedConnections >= connectionsToEstablish) break
        }

        return circuits.answer
    }

    fun part2(input: List<String>): Long {
        val nodes = input
            .map(Node::parse)
            .withIndex()
            .map { (index, node) -> IdentifiableNode(index, node) }

        val connections = buildList {
            for (firstIndex in 0..<nodes.size) {
                for (secondIndex in firstIndex+1..<nodes.size) {
                    add(Connection(nodes[firstIndex], nodes[secondIndex]))
                }
            }
        }.sortedBy { it.distance }

        val circuits = Circuits()

        var lastConnection: Connection? = null

        for (connection in connections) {
            if (connection !in circuits) {
                circuits += connection
                lastConnection = connection
            }
        }
        return lastConnection?.secondNode?.x?.let { lastConnection.firstNode.x.times(it) } ?: 0
    }

    check(part1(readInput("Day08_test"), connectionsToEstablish=10) == 40)
    check(part2(readInput("Day08_test")) == 25272L)

    val input = readInput("Day08")
    part1(input, connectionsToEstablish=1000).println()
    part2(input).println()
}
