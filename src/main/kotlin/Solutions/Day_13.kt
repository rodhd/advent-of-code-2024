package Solutions

import Common.AoCSolution

class Day_13: AoCSolution() {
    override val day = "13"
    val listRegex = Regex("\\[([\\d\\,]*)\\]")

    override fun FirstSolution() {
        val input = readInputAsString().split("\n\n")
        val packets = input
            .map{x -> parsePacketPairs(x)}
            .map{x -> comparePackets(x).first}
        var result = packets.zip(packets.indices) { a, b -> if(a) b + 1 else 0}.sum()
        println(result)
    }

    override fun SecondSolution() {
        val input = readInputAsString().split("\n\n")
        val decoderKey1 = parsePacket("[[2]]")
        val decoderKey2 = parsePacket("[[6]]")
        val packets_left = input
            .map{x -> parsePacketPairs(x).first}
            .toMutableList()
        val packets_right = input
            .map{x -> parsePacketPairs(x).second}
        packets_left.addAll(packets_right)
        packets_left.add(decoderKey1)
        packets_left.add(decoderKey2)
        packets_left.sortWith { a, b ->
            val res = comparePackets(Pair(a,b))
            if(res.first) -1 else 1
        }
        println((packets_left.indexOf(decoderKey1)+1) * (packets_left.indexOf(decoderKey2)+1))
    }

    sealed class Packet {

        data class ListsPacket(val packet: MutableList<Packet>) : Packet() {
            operator fun get(i: Int) = packet[i]

            fun getOrNull(i: Int) = packet.getOrNull(i)

            fun isEmpty() = packet.isEmpty()
            override fun toString(): String {
                return packet.toString()
            }
        }

        data class IntegerPacket(val packet: Int) : Packet() {
            fun toList() = ListsPacket(packet = mutableListOf(this))
            override fun toString(): String {
                return packet.toString()
            }
        }
    }

    fun parsePacketPairs(text: String): Pair<Packet, Packet> {
        val (left, right) = text.split("\n")
        return Pair(parsePacket(left), parsePacket(right))
    }

    fun parsePacket(text:String): Packet {
        var buffer = ""
        var result = Packet.ListsPacket(mutableListOf())
        var currentList = result
        var bufferList = mutableListOf<Packet>(result)
        for(i in 1..text.lastIndex) {
            if(text[i] == '[') {
                var newList = Packet.ListsPacket(mutableListOf())
                bufferList.add(newList)
                currentList.packet.add(newList)
                currentList = newList
            }
            else if(text[i].isDigit()) {
                buffer += text[i]
            }
            else if (text[i] == ',' && buffer.isNotEmpty()) {
                currentList.packet.add(Packet.IntegerPacket(buffer.toInt()))
                buffer = ""
            }
            else if (text[i] == ']') {
                if(buffer.isNotEmpty()) {
                    currentList.packet.add(Packet.IntegerPacket(buffer.toInt()))
                    buffer = ""
                }
                bufferList.removeLast()
                if(bufferList.isNotEmpty()) {
                    currentList = bufferList.last() as Packet.ListsPacket
                }
            }
        }
        return result
    }



    fun comparePackets(pair: Pair<Packet, Packet>): Pair<Boolean, Boolean> {
        var i =  0
        var (areLists, listPair) = bothAreLists(pair)
        if(areLists) {
            while(listPair.first.getOrNull(i) != null && listPair.second.getOrNull(i) != null) {
                //Both are integers
                val areIntegers = bothAreInteger(Pair(listPair.first[i], listPair.second[i]))
                if(areIntegers.first)
                {
                    if (areIntegers.second) {
                        i += 1
                        continue
                    }
                    else {
                        return Pair(areIntegers.third, true)
                    }

                }
                //One is integer
                val oneIsInteger = oneIsInteger(Pair(listPair.first[i], listPair.second[i]))
                if(oneIsInteger.second) {
                    val res = comparePackets(oneIsInteger.first)
                    if(res.second) {
                        return res
                    }
                    else {
                        i++
                        continue
                    }
                }
                //One list is empty
                val oneListIsEmpty = oneListIsEmpty(Pair(listPair.first[i], listPair.second[i]))
                if(oneListIsEmpty.second) {
                    return Pair(oneListIsEmpty.first, true)
                } else if(oneListIsEmpty.first && !oneListIsEmpty.second) {
                    i++
                    continue
                }
                //Both are lists
                val areLists = bothAreLists(Pair(listPair.first[i], listPair.second[i]))
                if(areLists.first) {
                    val res = comparePackets(areLists.second)
                    if(res.second) {
                        return res
                    }
                    else {
                        i++
                        continue
                    }
                }
            }
            if(listPair.first.getOrNull(i) == null && listPair.second.getOrNull(i) != null) return Pair(true, true)
            else if(listPair.first.getOrNull(i) != null && listPair.second.getOrNull(i) == null) return Pair(false, true)
            else return Pair(false, false)
        }
        return Pair(false, false)
    }

    fun bothAreInteger(pair: Pair<Packet, Packet>): Triple<Boolean,Boolean, Boolean> {
        val (left, right) = pair
        if(left is Packet.IntegerPacket && right is Packet.IntegerPacket) {
            if(left.packet < right.packet) return Triple(true, false, true)
            else if (left.packet > right.packet) return Triple(true, false, false)
            else if (left.packet == right.packet) return Triple(true, true, false)
        }
        return Triple(false, false, false)
    }

    fun oneListIsEmpty(pair: Pair<Packet, Packet>): Pair<Boolean, Boolean> {
        val (left, right) = pair
        if(left is Packet.ListsPacket && right is Packet.ListsPacket) {
            if(left.isEmpty() && right.isEmpty()) {
                return Pair(true, false)
            }
            else if(left.isEmpty()) {
                return Pair(true, true)
            } else if(right.isEmpty()) {
                return Pair(false, true)
            }
        }
        return Pair(false, false)
    }

    fun bothAreLists(pair: Pair<Packet, Packet>): Pair<Boolean, Pair<Packet.ListsPacket, Packet.ListsPacket>> {
        val (left, right) = pair
        if(left is Packet.ListsPacket && right is Packet.ListsPacket){
            return Pair(true, Pair(left, right))
        }
        return Pair(false, Pair(Packet.ListsPacket(mutableListOf()), Packet.ListsPacket(mutableListOf())))
    }

    fun oneIsInteger(pair: Pair<Packet, Packet>): Pair<Pair<Packet.ListsPacket, Packet.ListsPacket>, Boolean> {
        val (left, right) = pair
        if(left is Packet.IntegerPacket && right is Packet.ListsPacket) {
            return Pair(Pair(left.toList(), right), true)
        }
        if(left is Packet.ListsPacket && right is Packet.IntegerPacket) {
            return Pair(Pair(left, right.toList()), true)
        }
        return Pair(Pair(Packet.ListsPacket(mutableListOf()), Packet.ListsPacket(mutableListOf())), false)
    }
}