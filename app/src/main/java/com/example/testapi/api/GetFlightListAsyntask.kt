package com.example.testapi.api

import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.testapi.database.AppDatabase
import com.example.testapi.database.model.MCS_Flight
import com.example.testapi.delegate.SendMessageDelegate
import com.example.testapi.util.Common
import com.example.testapi.util.WebServiceKey
import org.json.JSONArray
import org.json.JSONObject
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapPrimitive
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class GetFlightListAsyntask{
    var context:Context?=null
    var webServiceMessage:String=""
    private var delegate:SendMessageDelegate?=null

    constructor(context: Context){
        this.context=context
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getData() {

        if (Common.isConnect(context!!)!!) {
            callGetFlightAsynTask().execute()

        } else {
            Toast.makeText(context,"no internet access",Toast.LENGTH_SHORT).show()
        }
    }

    inner class callGetFlightAsynTask: AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {
            var methodName = "GetFlight"
            val soapObject = SoapObject("http://tempuri.org/",methodName)
            // soapObject.addProperty(WebServiceKey.SignKey.key_sign, Common.getSHAKey(context!!))
            val soapSerializationEnvelope= SoapSerializationEnvelope(SoapEnvelope.VER11)
            soapObject.addProperty("sign", context?.let { Common.getSHKey(it) })
            soapObject.addProperty("ModifiedDate","")
            soapSerializationEnvelope.dotNet = true
            soapSerializationEnvelope.setOutputSoapObject(soapObject)

            val httpTransportSE= HttpTransportSE(WebServiceKey.web_service_url_prefix+ WebServiceKey.web_service_url_postfix)
            httpTransportSE.debug = true
            httpTransportSE.call("http://tempuri.org/GetFlight",soapSerializationEnvelope)


            try {
                var webServiceResultList =soapSerializationEnvelope.response as SoapPrimitive
                Log.i("TAG","result = $webServiceResultList")

                var jsonObject= JSONObject(webServiceResultList.toString())
                var responseData=jsonObject.getString(WebServiceKey.key_response_data)
                var responseInfo=jsonObject.getString(WebServiceKey.key_response_info)
                try {
                    if (!responseData.equals("")) {
                        val jsonArray = JSONArray(responseData)
                        val database = AppDatabase.getDatabase(context = context!!)
                        for (i in 0 until jsonArray.length()) {
                            val jObject = jsonArray.getJSONObject(i)
                            val store = MCS_Flight()
                            store.FlightID = jObject.getString("FlightID")
                            store.FlightNo = jObject.getString("FlightNo")

                            database.getMcsFligntDAO().insert(store)

                            var testList = database.getMcsFligntDAO().getAllFlight()
                            Log.i("TAG", "Store: " + testList[0].FlightNo)
                            //  Log.i("TAG", "Machine: " + testList[1].id)
                        }
                    }
                }catch (e:java.lang.Exception){

                }

                if (!responseInfo.equals("")) {
                    var j_bject = JSONObject(responseInfo)
//                    response_info_code = j_bject.getString(WebServiceKey.KeyForResponse.key_code)
//                    response_info_message = j_bject.getString(WebServiceKey.KeyForResponse.key_message)
                }

                webServiceMessage = "success"
            } catch (e: Exception) {
                webServiceMessage=e.message.toString()
            }


        return webServiceMessage

    }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (delegate!=null){
                delegate!!.sendReplyMessage(webServiceMessage)
            }
            if (webServiceMessage == "success") {
                Toast.makeText(context, "Success Store", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error Store", Toast.LENGTH_SHORT).show()
            }
        }


            fun sendReply(sendMessageDelegate: SendMessageDelegate){
                delegate=sendMessageDelegate
            }


    }

}