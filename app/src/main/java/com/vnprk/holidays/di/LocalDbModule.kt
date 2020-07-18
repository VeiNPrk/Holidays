package com.vnprk.holidays.di

import android.content.Context
import androidx.room.Room
import com.vnprk.holidays.Room.DBRoomInp
import com.vnprk.holidays.Room.DaoInp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class LocalDbModule {

    @Provides
    @Singleton
    fun provideDBRoomInp(context: Context): DBRoomInp {
        return Room.databaseBuilder(context, DBRoomInp::class.java, "database")
            //.addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideDaoInp(db: DBRoomInp): DaoInp {
        return db.inpDao()
    }
}