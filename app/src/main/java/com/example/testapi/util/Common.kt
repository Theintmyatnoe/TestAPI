package com.example.testapi.util

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.security.MessageDigest

class Common {

    companion object{

        @RequiresApi(Build.VERSION_CODES.M)
        fun isConnect(context: Context):Boolean?{
            val cm=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo=cm.activeNetworkInfo
            return networkInfo!=null && networkInfo.isConnectedOrConnecting
        }

        fun getSHKey(context: Context): String? {
            var keyHash:String?=""
            try {
                val info=context.packageManager.getPackageInfo(context.packageName,PackageManager.GET_SIGNATURES)
                Log.i("package_name",context.packageName)
                for (signature in info.signatures){
                    val md=MessageDigest.getInstance("MD5")
                    md.update(signature.toByteArray())
                    Log.i("md",md.digest().toString())
                    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.FROYO){
                        keyHash=android.util.Base64.encodeToString(md.digest(),android.util.Base64.DEFAULT)
                    }
                }
            }
            catch (e:PackageManager.NameNotFoundException){
                keyHash=e.message
            }
            catch (e:NoSuchFieldException){
                keyHash=e.message
            }
            catch (e: Exception){
                keyHash=e.message
            }
            return keyHash?.trim()
        }
    }


}