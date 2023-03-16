package com.dicoding.kotlin

import com.dicoding.kotlin.oop.Animal
import com.dicoding.kotlin.oop.Animal2

class App {
}
fun oop() {
    val dicodingCat = Animal("Dicoding Miaw", 4.2, 2, true)
    println("Nama: ${dicodingCat.name}, Berat: ${dicodingCat.weight}, Mamalia: ${dicodingCat.isMammal}")
    dicodingCat.eat()
    dicodingCat.sleep()

    val dicodingCatt = Animal2()
    println("Nama: ${dicodingCatt.name}")
    dicodingCatt.name = "Goose"
    println("Nama: ${dicodingCatt.name}")
}

fun main() {
    // println("Hello Kotlin!"); println("dsfjhdf")

    // print("\n>>> fundamentals\n")
    // Fundamentals().main()

    // print("\n>>> control flow\n")
    // ControlFlow().main()

    // print("\n>>> data classes and collections\n")
    // DataClassesAndCollections().main()

    // print("\n>>> functional programming\n")
    // FunctionalProgramming().main()

    oop()
}