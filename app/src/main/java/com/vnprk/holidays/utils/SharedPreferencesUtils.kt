package com.vnprk.holidays.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity

class SharedPreferencesUtils(){
    companion object {
        val NAME_PREFS = "HOLIDAY_PREF"
        val NAME_PREF_TOKEN = "token"
        val NAME_PREF_USER_ID = "my_user_id"
        val NAME_PREF_USER_ENT = "my_user_ent"

        val NAME_PREF_VERSION_DB = "my_version_db"
        val NAME_PREF_DATE_UPDATE_DB = "my_date_update_db"
        val NAME_PREF_HAS_NEW_VERSION_DB = "my_has_new_version_db"

        fun isNetworkAvailable(context:Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }

        fun getSharedPreferences(context:Context?){
             context?.getSharedPreferences(NAME_PREFS, AppCompatActivity.MODE_PRIVATE)
        }
/*
        fun getString(context:Context, namePrefs:String) = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).getString(namePrefs, "")

        fun getInt(context:Context, namePrefs:String) = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).getInt(namePrefs, 0)
*/
        fun getVersionDb(context:Context) = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).getInt(NAME_PREF_VERSION_DB, 0)

        fun setVersionDb(context:Context, value:Int){
            val editor = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).edit()
            editor.putInt(NAME_PREF_VERSION_DB, value)
            editor.apply()
        }

        fun getHasNewVersionDb(context:Context) = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).getBoolean(NAME_PREF_HAS_NEW_VERSION_DB, true)

        fun setHasNewVersionDb(context:Context, value:Boolean){
            val editor = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).edit()
            editor.putBoolean(NAME_PREF_HAS_NEW_VERSION_DB, value)
            editor.apply()
        }

        fun getDateUpdateDb(context:Context) = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).getString(
            NAME_PREF_DATE_UPDATE_DB, "")

        fun setDateUpdateDb(context:Context, value:String){
            val editor = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).edit()
            editor.putString(NAME_PREF_DATE_UPDATE_DB, value)
            editor.apply()
        }
        /*fun setUser(context:Context, user: UserClass){
          val editor = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).edit()
          editor.putInt(NAME_PREF_USER_ID, user.id)
          editor.apply()
          editor.putInt(NAME_PREF_USER_ENT, user.idEnt)
          editor.apply()
        }*/

        fun setString(context:Context, namePrefs:String, value:String){
          val editor = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).edit()
          editor.putString(namePrefs, value)
          editor.apply()
        }
    }
}