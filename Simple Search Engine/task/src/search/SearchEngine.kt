package search

import java.io.File
import java.lang.IllegalArgumentException

class SearchEngine {
    private var people = emptyList<String>()
    private val invertedIndex = mutableMapOf<String, MutableSet<Int>>()

    private enum class Strategies {
        ALL, ANY, NONE
    }

    fun searchInFile(file: File) {
        people = file.readLines()
        for (p in people)
            p.split(" ").forEach {
                invertedIndex.getOrPut(it.uppercase()) { mutableSetOf() }.add(people.indexOf(p))
            }
        while(true) {
            printMenu()
            when (readLine()!!) {
                "1" -> find()
                "2" -> people.forEach(::println)
                "0" -> {
                    println("Bye!")
                    break
                }
                else -> println("Incorrect option! Try again.")
            }
        }
    }

    private fun printMenu() {
        println("=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit")
    }

    private fun find() {
        println("Select a matching strategy: ALL, ANY, NONE")
        try {
            val strategy = Strategies.valueOf(readLine()!!)
            println("Enter a name or email to search all suitable people.")
            find(readLine()!!.uppercase().split(" "),strategy)
        } catch(e: IllegalArgumentException) {
            println("Unknown strategy")
        }
    }

    private fun find(query: List<String>, strategy: Strategies) {
        when (strategy) {
            Strategies.ALL -> findAll(query)
            Strategies.ANY -> findAny(query)
            Strategies.NONE -> findNone(query)
        }.forEach(::println)
    }

    private fun findAll(query: List<String>):List<String> {
        val includeIndexes = mutableSetOf<Int>()
        for (q in query)
            includeIndexes.intersect(invertedIndex[q.uppercase()] ?: emptySet())
        return people.filterIndexed { index, _ -> index in includeIndexes}
    }

    private fun findAny(query: List<String>):List<String> {
        return people.filterIndexed { index, _ -> index in getOccurrenceIndexes(query)}
    }

    private fun findNone(query: List<String>):List<String> {
        return people.filterIndexed { index, _ -> index !in getOccurrenceIndexes(query)}
    }

    private fun getOccurrenceIndexes(query: List<String>):IntArray {
        return invertedIndex
            .filter { it.key in query }
            .flatMap { it.value }
            .toIntArray()
    }
}