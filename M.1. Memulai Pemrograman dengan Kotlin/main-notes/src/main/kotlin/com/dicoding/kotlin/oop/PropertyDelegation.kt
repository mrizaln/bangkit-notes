package com.dicoding.kotlin.oop

import kotlin.reflect.KProperty

// property delegation: reduce getter and setter boilerplate

class DelegateName {
    private var value: Any = "Default"

    operator fun getValue(classRef: Any, property: KProperty<*>): Any {
        println("Fungsi ini sama seperti getter untuk properti ${property.name} pada class ${classRef}")
        return value
    }

    operator fun setValue(classRef: Any, property: KProperty<*>, newValue: Any) {
        println("Fungsi ini sama seperti setter untuk properti ${property.name} pada class ${classRef}")
        println("Nilai ${property.name} dari: ${value} akan berubah menjadi ${newValue}")
        value = newValue
    }
}
//class Animal3 {
//    var name: String by DelegateName()
//    var weight: Double by DelegateName()
//    var age: Int by DelegateName()
//}
//class Person {
//    var name: String by DelegateName()
//}
//class Hero {
//    var name: String by DelegateName()
//}
//
//fun main() {
//    val animal = Animal3()
//    animal.name = "Dicoding Miaw"
//    println("Nama Hewan: ${animal.name}")
//
//    val person = Person()
//    person.name = "Dimas"
//    println("Nama Orang: ${person.name}")
//
//    val hero = Hero()
//    hero.name = "Gatotkaca"
//    println("Nama Pahlawan: ${hero.name}")
//}