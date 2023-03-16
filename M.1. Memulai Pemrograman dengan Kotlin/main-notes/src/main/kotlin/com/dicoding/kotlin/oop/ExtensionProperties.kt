package com.dicoding.kotlin.oop

class Animal4(var name: String, var weight: Double, var age: Int)

val Animal.getAnimalInfo: String
    get() = "Name: ${this.name}, Berat: ${this.weight}, Umur: ${this.age}"

fun main() {
    val dicodingCat = Animal("Dicoding Miaw", 5.0, 2, true)
    println(dicodingCat.getAnimalInfo)
}
