package com.vnprk.holidays

import android.app.Application
import androidx.room.Room
import com.vnprk.holidays.Room.DBRoomInp
//import com.vnprk.holidays.Room.MIGRATION_1_2
import com.vnprk.holidays.di.ApplicationComponent
import com.vnprk.holidays.di.DaggerApplicationComponent
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

public class App : Application() {

    //private val instance: App

    val MAINURL_API="https://sert.pknk.ru"
    //private var database: DBRoomInp? = null
    @Inject
    lateinit var repository : Repository
    /*private lateinit var retrofit : Retrofit
    private lateinit var inpApi:InpApi*/

    companion object {
        lateinit var instance: App
            private set
    }
/*
    init{
        instance = this
    }*/

   // private var appToken :String? = "";
   lateinit var appComponent: ApplicationComponent
    val appContext: App
        get() = applicationContext as App

    
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
        instance = this
        appComponent.inject(this)

       /* database = Room.databaseBuilder(this, DBRoomInp::class.java, "database")
            //.addMigrations(MIGRATION_1_2)
            .build()*/

        //val gson = GsonBuilder().setLenient().create()
        /*retrofit = Retrofit.Builder()
                .baseUrl(MAINURL_API)
                 .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        inpApi = retrofit.create(InpApi::class.java)*/
        //repository = Repository()
    }

    fun getInstance(): App {
        return instance
    }

    //fun getApi() = inpApi

}