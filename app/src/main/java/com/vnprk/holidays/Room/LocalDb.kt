package com.vnprk.holidays.Room

import androidx.lifecycle.LiveData
import com.vnprk.holidays.models.*
import javax.inject.Inject

class LocalDb @Inject constructor(private val inpDao: DaoInp) {
    //private var inpDao: DaoInp
    init {
        //val db = App.instance.getDatabase()
        //inpDao = db!!.inpDao()

        //initTestData()
    }

    fun getAllHolidays(day:Int, month:Int, dayOfYear:Int) = inpDao.getAllHolidays(day, month, dayOfYear)

    fun getAllPrivateEvents() = inpDao.getAllPrivateEvents()

    fun disactiveEventById(id :Int){
        inpDao.disactiveEventById(id)
    }

    suspend fun updateAlarmHolidays(holidays: List<Holiday>){
        inpDao.updateAlarmHolidays(holidays)
    }

    fun getHolidayByType(types:List<Int>) = inpDao.getHolidayByType(types)

    fun getHolidayActiveAlarm() = inpDao.getHolidayActiveAlarm()

    fun getHolidaysByType(type:Int, day:Int, month:Int, dayOfYear:Int) = inpDao.getHolidaysByType(type, day, month, dayOfYear)

    fun getHolidayById(id:Int) = inpDao.getHolidayById(id)

    fun getHolidayId(id:Int) = inpDao.getHolidayId(id)

    fun getPrivateActiveAlarm() = inpDao.getPrivateActiveAlarm()

    fun getPrivateEventById(id:Int) = inpDao.getPrivateEventById(id)

    fun getPrivateById(id:Int) = inpDao.getPrivateById(id)

    suspend fun synchronizeHolidays(holidays: List<Holiday>){
        inpDao.synchronizeHolidays(holidays)
    }

    suspend fun savePrivateEvent(event: PrivateEvent) = inpDao.savePrivateEvent(event)

    suspend fun deletePrivateEvent(event: PrivateEvent){ inpDao.deletePrivateEvent(event) }
/*
    fun getNumDetail(idDetail:Int) = inpDao.getControlDetail(idDetail)

    fun getNotNumDetail(idDetail:Int) = inpDao.getControlNotNumDetail(idDetail)

    fun selectMyUser(token: String) : LiveData<UserClass> = inpDao.getMyUser(token)

    fun getChooseDetails() = inpDao.getTypesDetail()// detailsControlList

    fun getControlResults(typeDetail:Int) = inpDao.getControlResults(typeDetail)

    fun getFactorysDetail(typeDetail:Int) = inpDao.getFactorysDetail(typeDetail)

    fun getEntSpecialists() = inpDao.getEntSpecialists()

    fun getDefectTypes(typeDetail :Int) = inpDao.getDefectTypes(typeDetail)

    fun getDefectZones(typeDetail :Int) = inpDao.getDefectZones(typeDetail)

    fun getSteels() = inpDao.getSteels()

    fun getMethodNkTypes(typeDetail :Int) = inpDao.getMethodNkTypes(typeDetail)

    fun getDefectDetectorTypes() = inpDao.getDefectDetectorTypes()

    fun getNotLoadedControlDetails() = inpDao.getNotLoadedControlDetails()

    suspend fun getMaxId() =inpDao.getMaxIdDetails()

    suspend fun insertCastDetail(detail: DetailNum) {
        inpDao.insertControlDetail(detail)
    }

    suspend fun updateCastDetail(detail: DetailNum) {
        inpDao.updateControlDetail(detail)
    }

    suspend fun insertNotNumDetail(detail: DetailNotNum) {
        inpDao.insertNotNumDetail(detail)
    }

    suspend fun updateNotNumDetail(detail: DetailNotNum) {
        inpDao.updateNotNumDetail(detail)
    }

    suspend fun synchronizeCastDetails(oldDetails: List<DetailNum>, newDetails: List<DetailNum>){
        inpDao.synchronizeCastDetails(oldDetails, newDetails)
    }

    fun getNotLoadedNotNumDetails() = inpDao.getNotLoadedNotNumDetails()

    suspend fun synchronizeNotNumDetails(oldDetails: List<DetailNotNum>, newDetails: List<DetailNotNum>){
        inpDao.synchronizeNotNumDetails(oldDetails, newDetails)
    }

    suspend fun insertInit(
        types: List<TypeDetail>,
        factorys: List<FactoryDetail>,
        results: List<ControlResult>,
        specialists: List<EntSpecialist>,
        defectType : List<DefectType>,
        defectZone : List<DefectZone>,
        steel : List<Steel>,
        methodNkType : List<MethodNkType>,
        defectDetectorType : List<DefectDetectorType>
    ){
        inpDao.insertInit(
            types,
            factorys,
            results,
            specialists,
            defectType,
            defectZone,
            steel,
            methodNkType,
            defectDetectorType)
    }

    suspend fun updateIdDetail(oldDetail: DetailNum, newDetail: DetailNum){
        inpDao.updateIdDetail(oldDetail, newDetail)
    }

    suspend fun insertUser(user: UserClass){
        inpDao.insertUser(user)
    }*/
}