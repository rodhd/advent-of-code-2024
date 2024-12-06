package Solutions

import Common.AoCSolution

@Suppress("unused")
class Day_7: AoCSolution() {
    override val day = "7"

    override fun FirstSolution() {
        /*val input = readInputAsListOfLines()
        var fileSystem = FileSystem()
        for(l in input) {
            fileSystem.instructionParse(l)
        }
        fileSystem.navigator = fileSystem.navigator.getOutermost()
        var result = ResultHolder(0)
        var secondResult = SecondResultHolder()
        val totalFileSize = fileSystem.navigator.getFileSize(result, secondResult)
        val freeSpace = 70000000 - totalFileSize
        println(result)
        println(secondResult
            .directoryList
            .map{x -> Pair(x.key, x.value)}
            .sortedBy { x -> x.second }
            .find { x -> x.second >= 30000000 - freeSpace }
            !!.second)*/
    }

    override fun SecondSolution() {
        println("see above")
    }

    data class ResultHolder(var result: Int) {
        override fun toString(): String {
            return result.toString()
        }
    }

    data class SecondResultHolder(var directoryList: MutableMap<String, Int> = mutableMapOf<String, Int>())

    class FileSystem() {
        val directoryRegex = Regex("dir (\\w+)")
        val goToDirectoryRegex = Regex("\\$ cd (\\w+)")
        val fileRegex = Regex("(\\d+) ([\\w.]+)")
        //var navigator = Directory("/", null)

        fun instructionParse(instruction: String) {
            /*if(instruction == "\$ cd /") {
                navigator = navigator.getOutermost()
            }
            if(instruction == "\$ cd ..") {
                navigator = navigator.getParentDirectory()!!
            }
            if(directoryRegex.matches(instruction)) {
                navigator.addDirectory(directoryRegex.matchEntire(instruction)!!.groupValues[1])
            }
            if(fileRegex.matches(instruction)) {
                val matches = fileRegex.matchEntire(instruction)!!.groupValues
                navigator.addFile(Integer.parseInt(matches[1]), matches[2])
            }
            if(goToDirectoryRegex.matches(instruction)) {
                val matches = goToDirectoryRegex.matchEntire(instruction)!!.groupValues
                navigator = navigator.getSubdirectory(matches[1])
            }*/
        }
    }

    interface FileSystemObject {
        abstract val name: String
        abstract val parent: Directory?
        fun getFileSize(result: ResultHolder, secondResult: SecondResultHolder): Int
        fun getParentDirectory(): Directory? {
            return parent
        }

        /*fun getContentList(): Map<String, FileSystemObject>*/

        fun getOutermost(): Directory {
            if(parent == null) {
                return this as Directory
            }
            var t = getParentDirectory()
            while(t?.getParentDirectory() != null) {
                t = t.getParentDirectory()
            }
            return t!!
        }
    }

    class Directory(override val name: String, override val parent: Directory?): FileSystemObject {
        var content: MutableMap<String, FileSystemObject> = mutableMapOf()
        /*override fun getContentList(): Map<String, FileSystemObject> {
            val r = content
                .map{x -> x.value.getContentList()}
                .fold(mapOf<String, FileSystemObject>()) {acc, curr -> acc + curr}
            return r
        }*/

        fun getSubdirectory(name: String): Directory {
            return content[name] as Directory
        }

        fun addDirectory(name: String) {
            content[name] = Directory(name, parent = this)
        }

        fun addFile(size: Int, name: String) {
            content[name] = File(size = size, name = name, parent = this)
        }

        override fun getFileSize(result: ResultHolder, secondResult: SecondResultHolder): Int {
            val r = content.map{x -> x.value.getFileSize(result, secondResult)}.sum()
            if(r <= 100000) result.result += r
            secondResult.directoryList[name] = r
            return r
        }
    }

    class File(val size: Int, override val name: String, override val parent: Directory): FileSystemObject {
        override fun getFileSize(result: ResultHolder,secondResult: SecondResultHolder): Int {
            return size
        }
        /*override fun getContentList(): Map<String,FileSystemObject> {
            return mapOf(parent.name to this.parent)
        }*/
    }
}