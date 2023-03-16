package com.dicoding.kotlin.oop

// ArithmeticException
// NumberFormatException
// NullPointerException

fun main() {
    val someNullValue: String? = null
    lateinit var someMustNotNullValue: String

    try {
        someMustNotNullValue = someNullValue!!
    } catch (e: Exception) {
        println("Error: ${e.toString()}")
        someMustNotNullValue = "Nilai String Null"
    } finally {
        println(someMustNotNullValue)
    }
}