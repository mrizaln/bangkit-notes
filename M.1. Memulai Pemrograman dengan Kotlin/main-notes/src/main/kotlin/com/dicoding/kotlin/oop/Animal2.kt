package com.dicoding.kotlin.oop

// normally, getter and setter function will be created automatically
// however, we can also manually added them

class Animal2 {
    var name: String = "Dicoding Miaw"
        get() {
            println("Fungsi Getter terpanggil")
            return field
        }
        set(value) {
            println("Fungsi Setter terpanggil")
            field = value
        }
}