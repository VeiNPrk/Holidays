package com.vnprk.holidays.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vnprk.holidays.models.*

@Database(entities = [
    Holiday::class,
    PrivateEvent::class/*,
    DetailNotNum::class,
    TypeDetail::class,
    FactoryDetail::class,
    ControlResult::class,
    EntSpecialist::class,
    DefectType::class,
    DefectZone::class,
    Steel::class,
    MethodNkType::class,
    DefectDetectorType::class*/
], version = 1)
abstract class DBRoomInp : RoomDatabase() {
    abstract fun inpDao(): DaoInp
}
/*
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE FactoryDetail ADD COLUMN startYear INTEGER")
        database.execSQL("ALTER TABLE FactoryDetail ADD COLUMN endYear INTEGER")
    }
}*/