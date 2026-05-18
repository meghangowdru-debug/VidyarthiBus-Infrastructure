package com.vidyarthi.bus.tracker

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val locationRef = database.getReference("busLocation")

    suspend fun updateLocation(location: BusLocation) {
        try {
            locationRef.setValue(location).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
