package com.vnprk.holidays.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.MutableLiveData
import com.fmklab.fmkinp.NetworkState

class NetworkUtils(){
    companion object {
        const val ERROR_NO_CONNECTION = "error_no_connection"
        val networkState = MutableLiveData<NetworkState>()

        fun isNetworkAvailable(context:Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
    }
}