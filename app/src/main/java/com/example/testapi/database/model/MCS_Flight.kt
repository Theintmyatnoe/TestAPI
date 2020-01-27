package com.example.testapi.database.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MCS_Flight (
    @PrimaryKey
    @NonNull
    var FlightID:String="",
    var FlightNo:String?="",
    var AirLineID:String?="",
    var Remark:String?="",
    var Active:String="",
    var CreatedBy:String="",
    var CreatedOn:String="",
    var ModifiedBy:String="",
    var ModifiedOn: String="",
    var LastAction:String=""
){}