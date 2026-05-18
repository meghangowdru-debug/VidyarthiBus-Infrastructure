package com.vidyarthi.bus.tracker

data class BusLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val speed: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)
