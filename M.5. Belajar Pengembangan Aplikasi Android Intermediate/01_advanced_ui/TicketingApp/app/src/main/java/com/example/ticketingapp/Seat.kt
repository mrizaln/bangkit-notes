package com.example.ticketingapp

data class Seat(
    val id: Int,
    var x: Float? = 0.0f,
    var y: Float? = 0.0f,
    var name: String,
    var isBooked: Boolean,
)