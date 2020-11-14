package com.vnprk.holidays.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import android.text.BoringLayout
import androidx.appcompat.app.AppCompatActivity

class SharedPreferencesUtils(){
    companion object {
        val NAME_PREFS = "HOLIDAY_PREF"
        val NAME_PREF_HOLIDAY_TIME = "setting_notify_holiday_time"
        val NAME_PREF_HOLIDAY_TYPES = "setting_notify_holiday_types"
        val NAME_PREF_INNER_REPEATS = "setting_inner_repeats"
        val NAME_PREF_FROM_NOTIF = "from_notif"

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

        /*fun setHasNewVersionDb(context:Context, value:Boolean){
            val editor = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).edit()
            editor.putBoolean(NAME_PREF_HAS_NEW_VERSION_DB, value)
            editor.apply()
        }*/

        fun getDateUpdateDb(context:Context) = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).getString(
            NAME_PREF_DATE_UPDATE_DB, "")

        fun setDateUpdateDb(context:Context, value:String){
            val editor = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).edit()
            editor.putString(NAME_PREF_DATE_UPDATE_DB, value)
            editor.apply()
        }

        fun getInnerRepeats(context:Context) = PreferenceManager.getDefaultSharedPreferences(context).getString(
            NAME_PREF_INNER_REPEATS, "5").toInt()

        fun getNotifyHolidayTime(context:Context) = PreferenceManager.getDefaultSharedPreferences(context).getLong(
            NAME_PREF_HOLIDAY_TIME, 0)

        fun getHolidayTipes(context:Context) = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(
            NAME_PREF_HOLIDAY_TYPES, emptySet())
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

        fun setBoolean(context:Context, namePrefs:String, value:Boolean){
            val editor = context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).edit()
            editor.putBoolean(namePrefs, value)
            editor.apply()
        }

        fun getBoolean(context:Context, namePrefs:String)= context.getSharedPreferences(NAME_PREFS, MODE_PRIVATE).getBoolean(
            namePrefs, false)
    }
}