package com.vnprk.holidays

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.work.ListenableWorker
import com.vnprk.holidays.models.LoadingState
import com.vnprk.holidays.models.Status
import com.vnprk.holidays.models.*
import java.lang.Exception
import com.vnprk.holidays.Room.LocalDb
import com.vnprk.holidays.Room.RemoteDb
import com.vnprk.holidays.models.Holiday
import com.vnprk.holidays.utils.DateUtils
import com.vnprk.holidays.utils.NetworkUtils.Companion.networkState
import com.vnprk.holidays.utils.SharedPreferencesUtils
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val localDb: LocalDb, private val remoteDb: RemoteDb) {

    //private val networkState = MutableLiveData<NetworkState>()
    private val TAG = "Repository"
    private var testHoliday = MutableLiveData<List<Event>>()
    private val _nowDate = MutableLiveData<Long>()
    val nowDate: LiveData<Long>
        get() = _nowDate

    init{
        _nowDate.value=Calendar.getInstance().timeInMillis
    }

    fun getNowDateLong() = nowDate
/*
    private var _myUser = MutableLiveData<UserClass>()
    private lateinit var myUser: LiveData<UserClass>
    //private var inpDao: DaoInp

    private val networkStateAuthorization = MutableLiveData<NetworkState>()
    private val networkStateSendCode = MutableLiveData<NetworkState>()
    val numTypes = arrayOf(6, 7, 11, 12)
    val notNumTypes = arrayOf(3, 4, 5, 8, 9, 10, 13, 14, 15, 16, 17, 18)

    private var searchDetailLiveData = MutableLiveData<Detail>()
    private lateinit var detailLiveData: LiveData<List<Detail>>
    private val _searchDetailsLiveData = MutableLiveData<List<Detail>>()
    var searchDetailsLiveData: LiveData<List<Detail>>
        get() = _searchDetailsLiveData
        set(value) {}

    val searchFilterStr = MutableLiveData<String>();

    val TAG = "Repository"
    //var detailsControlList = ArrayList<TypeDetail>()
    var detailsList = ArrayList<DetailNum>()
*/

    fun setNowDate(nowDate:Long){
        _nowDate.value=nowDate
    }

    fun getAllHolidays(day:Int, month:Int, dayOfYear:Int) = /*Transformations.switchMap(nowDate) {
            address -> */localDb.getAllHolidays(day, month, dayOfYear) //}

    fun getHolidaysByType(type:Int, day:Int, month:Int, dayOfYear:Int) = localDb.getHolidaysByType(type, day, month, dayOfYear)

    fun getHolidayById(id:Int) = localDb.getHolidayById(id)

    fun getAllPrivateEvents() = localDb.getAllPrivateEvents()

    fun getPrivateEventById(id:Int) = localDb.getPrivateEventById(id)

    suspend fun syncronizeHolidaysData(context: Context): ListenableWorker.Result {
        Log.d("coroutine", "initData Start")
        networkState.postValue(LoadingState.LOADING)
        val response = remoteDb.getAllHolidays()
        Log.d("coroutine", "remoteDb.getAllHolidays() DONE")
        try {
            if (response.isSuccessful && response.code() == 200) {
                val responseData: ResponseData<Holiday>? =
                    response.body()
                return if (responseData!!.isSucces == 1) {
                    localDb.synchronizeHolidays(responseData.result)
                    SharedPreferencesUtils.setVersionDb(context, responseData.versionDb)
                    SharedPreferencesUtils.setDateUpdateDb(context, responseData.dateUpdateDb)
                    SharedPreferencesUtils.setHasNewVersionDb(context, false)
                    Log.d("coroutine", "localDb.insertInit DONE")
                    networkState.postValue(LoadingState(Status.SUCCESS, ""))
                    ListenableWorker.Result.success()
                } else {
                    networkState.postValue(LoadingState(Status.FAILED, responseData.message))
                    ListenableWorker.Result.retry()
                }
            } else {
                Log.e(TAG, response.message())
                networkState.postValue(LoadingState(Status.FAILED, response.message()))
                return ListenableWorker.Result.retry()
            }
        } catch (e: Exception) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(LoadingState(Status.FAILED, errorMessage!!))
            return ListenableWorker.Result.failure()
        } catch (e: Throwable) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(LoadingState(Status.FAILED, errorMessage!!))
            return ListenableWorker.Result.failure()
        }
    }

    suspend fun saveEvent(event: PrivateEvent){
        localDb.savePrivateEvent(event)
    }

   // fun getNetworkState() = networkState

    /*fun syncronizeHolidayData() {
        TODO("Not yet implemented")
    }*/
/*
    fun getControlResults(typeDetail: Int) = localDb.getControlResults(typeDetail)

    fun getFactorysDetail(typeDetail: Int) = localDb.getFactorysDetail(typeDetail)

    fun getEntSpecialsts() = localDb.getEntSpecialists()

    fun getDefectTypes(typeDetail: Int) = localDb.getDefectTypes(typeDetail)

    fun getDefectZones(typeDetail: Int) = localDb.getDefectZones(typeDetail)

    fun getSteels() = localDb.getSteels()

    fun getMethodNkTypes(typeDetail: Int) = localDb.getMethodNkTypes(typeDetail)

    fun getDefectDetectorTypes() = localDb.getDefectDetectorTypes()

    fun getControlDetailsLiveData() = detailLiveData

    suspend fun searchDetails(
        type: Int,
        idEntUser: Int,
        searchString: String = ""
    ) {
        //val list = ArrayList<Detail>()
        _searchDetailsLiveData.postValue(ArrayList<Detail>())
        networkState.postValue(NetworkState.LOADING)
        try {
            val response = if (numTypes.contains(type)) remoteDb.getSearchNumDetails(
                type,
                idEntUser,
                searchString
            )
            else remoteDb.getSearchNotNumDetails(type, idEntUser, searchString)

            if (response.isSuccessful && response.code() == 200) {
                //searchDetailsLiveData.postValue((response.body() as ResponseData<Detail>).result)
                networkState.postValue(NetworkState(Status.SUCCESS, ""))
                _searchDetailsLiveData.postValue((response.body() as ResponseData<Detail>).result)

                //return searchDetailsLiveData
            } else {
                Log.e(TAG, response.message())
                networkState.postValue(NetworkState(Status.FAILED, response.message()))
                //return null
            }
        } catch (e: Exception) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(NetworkState(Status.FAILED, errorMessage!!))
            //return null
        } catch (e: Throwable) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(NetworkState(Status.FAILED, errorMessage!!))
            //return null
        }
    }


    fun initDetailLiveData(type: Int, idEntUser: Int) {

        detailLiveData = Transformations.switchMap(searchFilterStr) { input ->
            if (input == null || input.equals("") || input.equals("%%")) {
                //check if the current value is empty load all data else search
                Log.d(TAG, "detailLiveData Transformations.switchMap input null")
                /*return*/ if (numTypes.contains(type)) localDb.getNumDetails(
                    type,
                    idEntUser
                ) as LiveData<List<Detail>> else localDb.getNotNumDetails(
                    type,
                    idEntUser
                ) as LiveData<List<Detail>>/*new LivePagedListBuilder<>(
                                    teamDao.loadAllTeam(), config)
                                    .build();*/
            } else {
                Log.d(TAG, "detailLiveData Transformations.switchMap input $input")
                searchDetailsLiveData
            }

        }
    }

    fun getControlDetailByid(id: Int, type: Int, isSearch: Boolean): LiveData<Detail> {
        //if(isConnect){
        return if (isSearch) {
            if (detailLiveData.value != null) {
                searchDetailLiveData.postValue(detailLiveData.value?.find { it.id == id })
            } else {
                searchDetailLiveData.postValue(null)
            }
            searchDetailLiveData
        } else {
            if (numTypes.contains(type)) localDb.getNumDetail(id) as LiveData<Detail>
            else localDb.getNotNumDetail(id) as LiveData<Detail>
        }
        // }
    }

    suspend fun syncronizeData(userId: Int, userEnt: Int): ListenableWorker.Result {
        Log.d("coroutine", "syncronizeData Start")
        networkState.postValue(NetworkState.LOADING)
        val syncDetails = localDb.getNotLoadedControlDetails()
        val jsonData: String = Gson().toJson(syncDetails)
        Log.d("coroutine", "userId=$userId, userEnt=$userEnt json= $jsonData")

        try {
            val response = remoteDb.synchronizeCastData(userId, userEnt, jsonData)
            Log.d("coroutine", "synchronizeCastData DONE")
            if (response.isSuccessful && response.code() == 200) {
                val responseData: ResponseData<DetailNum>? = response.body()
                if (responseData!!.isSucces == 1) {
                    localDb.synchronizeCastDetails(syncDetails, responseData.result)
                    Log.d("coroutine", "synchronizeCastDetails DONE")
                    val syncNotNumDetails = localDb.getNotLoadedNotNumDetails()
                    val jsonNotNumData: String = Gson().toJson(syncNotNumDetails)
                    val responseNotNum =
                        remoteDb.synchronizeNotNumData(userId, userEnt, jsonNotNumData)
                    Log.d("coroutine", "synchronizeNotNumData DONE")
                    if (responseNotNum.isSuccessful && responseNotNum.code() == 200) {
                        val responseData: ResponseData<DetailNotNum>? = responseNotNum.body()
                        if (responseData!!.isSucces == 1) {
                            //inpDao.insertCastDetails(responseData.result)
                            localDb.synchronizeNotNumDetails(syncNotNumDetails, responseData.result)
                            //inpDao!!.getControlDetails()
                            Log.d("coroutine", "synchronizeNotNumDetails DONE")
                            networkState.postValue(NetworkState(Status.SUCCESS, ""))
                            return ListenableWorker.Result.success()
                        } else {
                            networkState.postValue(
                                NetworkState(
                                    Status.FAILED,
                                    responseData.message
                                )
                            )
                            return ListenableWorker.Result.retry()
                        }
                    } else {
                        Log.e(TAG, response.message())
                        networkState.postValue(NetworkState(Status.FAILED, response.message()))
                        return ListenableWorker.Result.retry()
                    }
                    //return ListenableWorker.Result.success()
                } else {
                    networkState.postValue(NetworkState(Status.FAILED, responseData.message))
                    return ListenableWorker.Result.retry()
                }
            } else {
                Log.e(TAG, response.message())
                networkState.postValue(NetworkState(Status.FAILED, response.message()))
                return ListenableWorker.Result.retry()
            }
        } catch (e: Exception) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(NetworkState(Status.FAILED, errorMessage!!))
            return ListenableWorker.Result.failure()
        } catch (e: Throwable) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(NetworkState(Status.FAILED, errorMessage!!))
            return ListenableWorker.Result.failure()
        }
    }

    suspend fun syncronizeNsiData(idEnt: Int) {
        Log.d("coroutine", "initData Start")
        networkState.postValue(NetworkState.LOADING)
        val response = remoteDb.getInitData(idEnt)
        Log.d("coroutine", "remoteDb.getInitData($idEnt) DONE")
        try {
            if (response.isSuccessful && response.code() == 200) {
                val responseInitData: ResponseInitData? = /*Log.d(TAG,*/
                    response.body()/*.toString())*/
                if (responseInitData!!.isSucces == 1) {

                    localDb.insertInit(
                        responseInitData.types,
                        responseInitData.factorys,
                        responseInitData.results,
                        responseInitData.specialists,
                        responseInitData.typesDeffect,
                        responseInitData.zones,
                        responseInitData.steel,
                        responseInitData.typesMethodNk,
                        responseInitData.typesDefectDetector
                    )

                    Log.d("coroutine", "localDb.insertInit DONE")
                    networkState.postValue(NetworkState(Status.SUCCESS, ""))
                } else {
                    networkState.postValue(NetworkState(Status.FAILED, responseInitData.message))
                }
            } else {
                Log.e(TAG, response.message())
                networkState.postValue(NetworkState(Status.FAILED, response.message()))
            }
        } catch (e: Exception) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(NetworkState(Status.FAILED, errorMessage!!))
        } catch (e: Throwable) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(NetworkState(Status.FAILED, errorMessage!!))
        }
    }

    //suspend fun getControlDetails() = inpDao.getControlDetails()

    suspend fun getMaxId() = localDb.getMaxId()

    suspend fun insertCastDetail(detail: DetailNum) {
        localDb.insertCastDetail(detail)
    }

    suspend fun updateCastDetail(detail: DetailNum) {
        localDb.updateCastDetail(detail)
    }

    suspend fun insertNotNumDetail(detail: DetailNotNum) {
        localDb.insertNotNumDetail(detail)
    }

    suspend fun updateNotNumDetail(detail: DetailNotNum) {
        localDb.updateNotNumDetail(detail)
    }

    fun authorization(login: String, pasw: String, token: String?) {
        Log.d(TAG, "authorization $login")
        //pokemonList.clear();
        networkStateAuthorization.postValue(NetworkState.LOADING)

        token?.let {
            remoteDb.authorization(login, pasw, token)
                .enqueue(object : Callback<ResponseClass> {
                    override fun onResponse(
                        call: Call<ResponseClass>,
                        response: Response<ResponseClass>
                    ) {
                        if (response.isSuccessful && response.code() == 200) {
                            val responseClass: ResponseClass? = response.body()
                            if (responseClass?.status == 1) {
                                networkStateAuthorization.postValue(NetworkState.LOADED)
                            } else {
                                networkStateAuthorization.postValue(
                                    NetworkState(
                                        Status.FAILED,
                                        responseClass!!.message
                                    )
                                )
                            }
                        } else {
                            Log.e(TAG, response.message())
                            networkStateAuthorization.postValue(
                                NetworkState(
                                    Status.FAILED,
                                    response.message()
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<ResponseClass>, t: Throwable) {
                        val errorMessage = t.message
                        Log.d(TAG, errorMessage)
                        networkStateAuthorization.postValue(
                            NetworkState(
                                Status.FAILED,
                                errorMessage!!
                            )
                        )
                    }
                })
        }
    }

    suspend fun sendRegistrationCode(code: String, token: String?) {
        Log.d(TAG, "sendRegistrationCode $code")
        networkStateSendCode.postValue(NetworkState.LOADING)
        token?.let {
            val response = remoteDb.sendAuthCode(code, token)
            //withContext(Dispatchers.Main) {
            try {
                if (response.isSuccessful && response.code() == 200) {
                    val user: UserClass? = response.body()
                    if (user?.id!! > 0) {
                        localDb.insertUser(user)
                        syncronizeNsiData(user.idEnt)
                        syncronizeData(user.id, user.idEnt)
                        networkStateSendCode.postValue(NetworkState.LOADED)
                    } else {
                        networkStateSendCode.postValue(NetworkState(Status.FAILED, user.name))
                    }
                } else {
                    Log.e(TAG, response.message())
                    networkStateSendCode.postValue(NetworkState(Status.FAILED, response.message()))
                }
            } catch (e: Exception) {
                val errorMessage = e.message
                Log.d(TAG, errorMessage)
                networkStateSendCode.postValue(NetworkState(Status.FAILED, errorMessage!!))
            } catch (e: Throwable) {
                val errorMessage = e.message
                Log.d(TAG, errorMessage)
                networkStateSendCode.postValue(NetworkState(Status.FAILED, errorMessage!!))
            }
        }
    }


    fun getNetworkStateAuthorization() = networkStateAuthorization
    fun getNetworkStateSendCode() = networkStateSendCode
*/
}