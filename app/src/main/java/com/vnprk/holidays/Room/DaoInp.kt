package com.vnprk.holidays.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vnprk.holidays.models.*

@Dao
/*interface*/abstract class DaoInp {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    /*@get:Query("SELECT * from signal ORDER BY time ASC")
    fun allSignalls: LiveData<List<UserClass>>*/

    @Query("SELECT * FROM holiday where ((day=:day and month=:month) or dayOfYear=:dayOfYear) order by ordered")
    abstract fun getAllHolidays(day:Int, month:Int, dayOfYear:Int): LiveData<List<Holiday>>

    @Query("SELECT * FROM holiday WHERE type=:type and ((day=:day and month=:month) or dayOfYear=:dayOfYear) order by ordered")
    abstract fun getHolidaysByType(type :Int, day:Int, month:Int, dayOfYear:Int): LiveData<List<Holiday>>

    @Query("SELECT * FROM holiday WHERE id=:id")
    abstract fun getHolidayById(id :Int): LiveData<Holiday>

    @Query("SELECT * FROM privateevent order by startDateTime")
    abstract fun getAllPrivateEvents(): LiveData<List<PrivateEvent>>

    @Query("SELECT * FROM privateevent WHERE id=:id")
    abstract fun getPrivateEventById(id :Int): LiveData<PrivateEvent>

    @Query("SELECT * FROM privateevent WHERE id=:id")
    abstract fun getPrivateById(id :Int): PrivateEvent
/*
    @Query("select h.id_holiday, h.name_holiday, h.description_holiday, h.img_holiday, h.type, h.country, h.day, h.month, h.dayOfYear, h.ordered from(SELECT row_number() over(partition by h.type order by h.ordered) as rown, h.* FROM holiday h)h")
    abstract fun getAllHolidays1(): LiveData<List<Holiday>>
*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun synchronizeHolidays(holidays: List<Holiday>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPrivateEvent(privateEvent: PrivateEvent)

    @Update
    abstract suspend fun updatePrivateEvent(privateEvent: PrivateEvent)

    @Transaction
    open suspend fun savePrivateEvent(privateEvent: PrivateEvent) {
        if(privateEvent.id>0)
            updatePrivateEvent(privateEvent)
        else
            insertPrivateEvent(privateEvent)
    }
/*
    @Query("SELECT * FROM typedetail where id in(3,4,5,6,7,8,9,10,11,12,13,14,15,16,17)")
    abstract fun getTypesDetail(): LiveData<List<TypeDetail>>

    @Query("SELECT * FROM factorydetail WHERE typeDetail=:type order by orderField")
    abstract fun getFactorysDetail(type :Int): LiveData<List<FactoryDetail>>

    @Query("SELECT * FROM detailnum order by id desc")
    abstract suspend fun getControlDetails(): List<DetailNum>

    @Query("SELECT * FROM detailnum where id=:id order by id desc")
    abstract suspend fun getControlDetailsByid(id:Int): List<DetailNum>

    @Query("SELECT * FROM detailnum where isLoaded=0 order by id desc")
    abstract fun getNotLoadedControlDetails(): List<DetailNum>

    @Query("SELECT * FROM detailnotnum where isLoaded=0 order by id desc")
    abstract fun getNotLoadedNotNumDetails(): List<DetailNotNum>

    @Query("SELECT max(id) FROM detailnum")
    abstract suspend fun getMaxIdDetails(): Int

    @Query("SELECT * FROM detailnum WHERE type=:typeDetail AND idEntUser=:idEntUser order by id desc")
    abstract fun getControlDetails(typeDetail :Int, idEntUser:Int): LiveData<List<DetailNum>>

    @Query("SELECT * FROM detailnotnum WHERE type=:typeDetail AND idEntUser=:idEntUser order by id desc")
    abstract fun getNotNumDetails(typeDetail :Int, idEntUser:Int): LiveData<List<DetailNotNum>>

    @Query("SELECT * FROM detailnum WHERE id=:idDetail")
    abstract fun getControlDetail(idDetail :Int): LiveData<DetailNum>

    @Query("SELECT * FROM detailnotnum WHERE id=:idDetail")
    abstract fun getControlNotNumDetail(idDetail :Int): LiveData<DetailNotNum>

    @Query("SELECT * FROM controlresult where typeDetail=:typeDetail order by orderBy")
    abstract fun getControlResults(typeDetail :Int): LiveData<List<ControlResult>>

    @Query("SELECT * FROM entspecialist order by lastName")
    abstract fun getEntSpecialists(): LiveData<List<EntSpecialist>>



    @Query("SELECT * FROM defecttype where typeDetail=:typeDetail order by id")
    abstract fun getDefectTypes(typeDetail :Int): LiveData<List<DefectType>>

    @Query("SELECT * FROM defectzone where typeDetail=:typeDetail order by orderBy")
    abstract fun getDefectZones(typeDetail :Int): LiveData<List<DefectZone>>

    @Query("SELECT * FROM steel")
    abstract fun getSteels(): LiveData<List<Steel>>

    @Query("SELECT * FROM methodnktype where typeDetail=:typeDetail")
    abstract fun getMethodNkTypes(typeDetail :Int): LiveData<List<MethodNkType>>

    @Query("SELECT * FROM defectdetectortype order by name")
    abstract fun getDefectDetectorTypes(): LiveData<List<DefectDetectorType>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUser(user: UserClass)

    @Insert/*(onConflict = OnConflictStrategy.IGNORE)*/
    abstract suspend fun insertControlDetail(detail: DetailNum)

    @Insert/*(onConflict = OnConflictStrategy.IGNORE)*/
    abstract suspend fun insertNotNumDetail(detail: DetailNotNum)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCastDetails(details: List<DetailNum>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNotNumDetails(details: List<DetailNotNum>)

    @Update
    abstract suspend fun updateControlDetail(detail: DetailNum)

    @Update
    abstract suspend fun updateNotNumDetail(detail: DetailNotNum)

    @Query("UPDATE detailnum SET id = :newId, isLoaded=1 WHERE numDetail = :num AND _dateControl = :dtControl")
    abstract suspend fun updateIdDetail(newId: Int, num: String?, dtControl: String): Int

    @Delete
    abstract suspend fun deleteControlDetail(detail: DetailNum)

    @Delete
    abstract suspend fun deleteCastDetails(details: List<DetailNum>)

    @Delete
    abstract suspend fun deleteNotNumDetails(details: List<DetailNotNum>)

    @Transaction
    open suspend fun insertInit(
        types: List<TypeDetail>,
        factorys: List<FactoryDetail>,
        results: List<ControlResult>,
        specialists: List<EntSpecialist>,
        defectType : List<DefectType>,
        defectZone : List<DefectZone>,
        steel : List<Steel>,
        methodNkType : List<MethodNkType>,
        defectDetectorType : List<DefectDetectorType>
    ) {
        insertInitTypeDetail(types)
        insertInitFactoryDetail(factorys)
        insertInitResult(results)
        insertInitSpecialist(specialists)
        insertInitDefectType(defectType)
        insertInitDefectZone(defectZone)
        insertInitSteel(steel)
        insertInitMethodNkType(methodNkType)
        insertInitDefectDetectorType(defectDetectorType)
    }

    @Transaction
    open suspend fun updateIdDetail(oldDetail: DetailNum, newDetail: DetailNum) {
        deleteControlDetail(oldDetail)
        insertControlDetail(newDetail)
    }

    @Transaction
    open suspend fun synchronizeCastDetails(oldDetails: List<DetailNum>, newDetails: List<DetailNum>) {
        deleteCastDetails(oldDetails)
        insertCastDetails(newDetails)
    }

    @Transaction
    open suspend fun synchronizeNotNumDetails(oldDetails: List<DetailNotNum>, newDetails: List<DetailNotNum>) {
        deleteNotNumDetails(oldDetails)
        insertNotNumDetails(newDetails)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitTypeDetail(types: List<TypeDetail>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitFactoryDetail(factorys: List<FactoryDetail>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitResult(results: List<ControlResult>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitSpecialist(specialists: List<EntSpecialist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitDefectType(defectType : List<DefectType>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitDefectZone(defectZone : List<DefectZone>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitSteel(steel : List<Steel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitMethodNkType(methodNkType : List<MethodNkType>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInitDefectDetectorType(defectDetectorType : List<DefectDetectorType>)*/
}