package com.dicoding.kotlin.nyoba

data class DataUser(val name: String, val address: String)

fun main() {
    val x = 11
    when (x) {
        10, 11 -> print("A")
        11, 12 -> print("B")
    }
    val dicoding = DataUser("Dicoding", "Bandung")
    println(dicoding)
}