package com.fmklab.fmkinp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel /*: AndroidViewModel */{

    /*constructor(application: Application) : super(application)

    @Inject
    lateinit var repository :Repository
    var message = ""
    var isVisibleProgress = false
    var isVisibleNoConnection = false
    var eventLiveData = MutableLiveData<EventClass>()
    private lateinit var lyfecycleOwner: LifecycleOwner

    val ACTION_GO_TO_REGISTRATION = "splash_viewmodel_action_go_to_reg"
    val ACTION_GO_TO_WORK = "splash_viewmodel_action_go_to_work"

    init {
        getApplication<App>().appComponent.inject(this)
        //repository = App.instance.getRepository()!!
    }

    fun setLifecycle(lifecycleOwner: LifecycleOwner) {
        this.lyfecycleOwner = lifecycleOwner
    }

    fun observeNetworkState(networkState: NetworkState) {
        networkState.let {
            when (networkState.status) {
                Status.RUNNING -> {
                    Log.d("coroutine", "Status.RUNNING")
                    isVisibleProgress = true
                    message = networkState.msg
                }
                Status.SUCCESS -> {
                    Log.d("coroutine", "Status.SUCCESS")
                    isVisibleProgress = false
                }
                Status.FAILED -> {
                    Log.d("coroutine", "Status.FAILED")
                    isVisibleProgress = false
                    message = networkState.msg
                }
            }
        }
    }

    fun checkConnection() {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            isVisibleNoConnection = true
            isVisibleProgress = false
            message = getApplication<Application>().getString(R.string.no_connection)
            eventLiveData.value = EventClass(Event.ERROR, NetworkUtils.ERROR_NO_CONNECTION)
        }
    }

    fun getMyUser(token: String) {
        //DateUtils.getLastNYears(50)
        repository.selectMyUser(token).observe(lyfecycleOwner, Observer { user ->
            //val userTest = UserClass(27,"",192,"")
            //SharedPreferencesUtils.setUser(getApplication<Application>(), userTest)
            /*editor.putInt("my_user_id", userTest.id)
            editor.apply()
            editor.putInt("my_user_ent", userTest.idEnt)
            editor.apply()*/
            //initData(userTest)
            if (user != null) {
                Log.d("selectMyUser", user.name)
                SharedPreferencesUtils.setUser(getApplication(), user)
                initData(user)
            } else
                if (NetworkUtils.isNetworkAvailable(getApplication()))
                    eventLiveData.value = EventClass(Event.ACTION, ACTION_GO_TO_REGISTRATION)
                else {
                    isVisibleNoConnection = true
                    isVisibleProgress = false
                    message = getApplication<Application>().getString(R.string.no_connection)
                    eventLiveData.value = EventClass(Event.ERROR, NetworkUtils.ERROR_NO_CONNECTION)

                }
        })
    }

    override fun onCleared() {
        super.onCleared()
        //repository = null
    }

    fun getNetworkState() = repository.getNetworkState()

    private fun initData(user: UserClass) {
        Log.d("coroutine", "initData1")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("coroutine", "initData2")
            if (NetworkUtils.isNetworkAvailable(getApplication())) {
                repository.syncronizeNsiData(user.idEnt)
                repository.syncronizeData(user.id, user.idEnt)
            }
            Log.d("coroutine", "initData2.1")
            eventLiveData.postValue(EventClass(Event.ACTION, ACTION_GO_TO_WORK))
            Log.d("coroutine", "initData3")
        }
        Log.d("coroutine", "initData4")
    }*/
}