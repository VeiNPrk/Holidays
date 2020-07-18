package com.vnprk.holidays.Room

import com.vnprk.holidays.InpApi
import javax.inject.Inject

class RemoteDb @Inject constructor(private val retrofit: InpApi) {
    //val retrofit = App.instance.getApi()

    suspend fun getAllHolidays() = retrofit.getHolidays()

    suspend fun getDbVersion() = retrofit.getDbVersion()
/*
    suspend fun getInitData(idEnt:Int) = retrofit.getInitData(idEnt)

    suspend fun synchronizeCastData(idUser:Int, idEnt:Int, detailsJson:String) = retrofit.synchronizeCastData(idUser, idEnt, detailsJson)

    suspend fun synchronizeNotNumData(idUser:Int, idEnt:Int, detailsJson:String) = retrofit.synchronizeNotNumData(idUser, idEnt, detailsJson)

    suspend fun getSearchNumDetails(type:Int, idEntUser:Int, searchString:String) = retrofit.getSearchNumDetails(type, idEntUser, searchString) //{

    suspend fun getSearchNotNumDetails(type:Int, idEntUser:Int, searchString:String)= retrofit.getSearchNotNumDetails(type, idEntUser, searchString) // {
*/

}