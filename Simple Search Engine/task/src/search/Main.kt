package search

import java.io.File

fun main(args: Array<String>) {
    try {
        val file = File(args[1])
        SearchEngine().searchInFile(file)
    } catch (e: Exception) {
        println("Wrong parameters or file path!")
        return
    }
}