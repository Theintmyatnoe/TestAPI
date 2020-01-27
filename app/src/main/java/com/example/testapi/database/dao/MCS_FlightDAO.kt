package com.example.testapi.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.testapi.database.model.MCS_Flight
@Dao
interface MCS_FlightDAO {
    @Insert
    fun insert(flights: MCS_Flight)

    @Query(value = "Select * from MCS_Flight")
    fun getAllFlight(): List<MCS_Flight>

}