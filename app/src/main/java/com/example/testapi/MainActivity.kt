package com.example.testapi

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.testapi.api.GetFlightListAsyntask
import com.example.testapi.database.AppDatabase
import com.example.testapi.delegate.SendMessageDelegate
import com.example.testapi.util.Common
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SendMessageDelegate {
    override fun sendReplyMessage(message: String) {
        if (message.isNotEmpty()){
            if (message=="success"){
                val appDatabase=AppDatabase.getDatabase(this)
                val flightList=appDatabase.getMcsFligntDAO().getAllFlight()
                if (flightList.isNotEmpty()){
                    for (flights in flightList){
                        flights.FlightNo?.let { strFlightList.add(it) }
                        val spinnerAdapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,strFlightList)
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        flight_spinner!!.adapter=spinnerAdapter
                    }

                }

            }
        }
    }

    private var strFlightList= arrayListOf<String>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getFlightListAsyntask=GetFlightListAsyntask(this)
        getFlightListAsyntask.getData()
        getFlightListAsyntask.callGetFlightAsynTask().sendReply(this)

        if (Common.isConnect(this)!!){
            Toast.makeText(this,"On Connected",Toast.LENGTH_SHORT).show()

        }
        else{
            Toast.makeText(this,"No Internet Access",Toast.LENGTH_SHORT).show()

        }

    }



}
