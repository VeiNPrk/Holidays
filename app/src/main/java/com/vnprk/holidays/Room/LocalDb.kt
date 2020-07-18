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

    fun getAllHolidays() = inpDao.getAllHolidays()

    fun getHolidaysByType(type:Int) = inpDao.getHolidaysByType(type)

    suspend fun synchronizeHolidays(holidays: List<Holiday>){
        inpDao.synchronizeHolidays(holidays)
    }
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