package com.vnprk.holidays.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.*
import com.vnprk.holidays.BR
import com.vnprk.holidays.R
import com.vnprk.holidays.databinding.ActivitySplashScreenBinding
import com.vnprk.holidays.MainActivity
import com.vnprk.holidays.models.HolidayWorker
import com.vnprk.holidays.models.LoadingState
import com.vnprk.holidays.models.Status
import com.vnprk.holidays.utils.NetworkUtils
import com.vnprk.holidays.utils.SharedPreferencesUtils
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity(){

    private val TAG = "SplashScreenActivity"
    //private var repository = App.instance.getRepository()
    //lateinit var viewModel: SplashScreenViewModel
    lateinit var binding : ActivitySplashScreenBinding
    var token:String=""
    var message = ""
    var isVisibleProgress = false
    var isVisibleNoConnection = false
    private val WORK_SAVE_NAME = "work_save_name"
    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    private val constraints1 = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    private val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        workManager.pruneWork()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.lifecycleOwner=this
        //viewModel = ViewModelProviders.of(this)[SplashScreenViewModel::class.java]
        //repository = App.instance.getRepository()!!
        //viewModel.setLifecycle(this)
        //token = SharedPreferencesUtils.getString(this, SharedPreferencesUtils.NAME_PREF_TOKEN)
        Log.e(TAG, token)

        NetworkUtils.networkState.observe(this, Observer { networkState ->
            networkState.let {
                when (networkState.status) {
                    Status.RUNNING -> {
                        Log.d("coroutine", "Status.RUNNING")
                        isVisibleProgress = true
                        isVisibleNoConnection=false
                        message = ""
                    }
                    Status.SUCCESS -> {
                        Log.d("coroutine", "Status.SUCCESS")
                        isVisibleProgress = false
                        isVisibleNoConnection=false
                        message = ""
                        goToWork()
                    }
                    Status.FAILED -> {
                        Log.d("coroutine", "Status.FAILED")
                        if(it.msg==NetworkUtils.ERROR_NO_CONNECTION) {
                            isVisibleProgress = false
                            isVisibleNoConnection=true
                            message = getString(R.string.no_first_connection)
                        }
                    }
                }
            }
            binding.setVariable(BR.view, this)
            binding.executePendingBindings()
            //viewModel.observeNetworkState(networkState)
        })
        refreshData()
                
        /*viewModel.eventLiveData.observe(this, Observer { event ->
            when (event.event) {
                Event.ACTION -> {
                    if(event.msg.equals(viewModel.ACTION_GO_TO_REGISTRATION))
                        goToRegistration()
                    if(event.msg.equals(viewModel.ACTION_GO_TO_WORK))
                        goToWork()
                }
                Event.ERROR -> {
                    if(event.msg.equals(NetworkUtils.ERROR_NO_CONNECTION)) {
                        binding.setVariable(BR.viewModel, viewModel)
                        binding.executePendingBindings()
                    }
                    //goToRegistration()
                }
                else -> {
                    print("")
                }
            }
        })*/
    }

    fun refreshData(){

        if(SharedPreferencesUtils.getVersionDb(this)==0){
            initHolidayData()
            if(!NetworkUtils.isNetworkAvailable(this))
                NetworkUtils.networkState.postValue(LoadingState(Status.FAILED, NetworkUtils.ERROR_NO_CONNECTION))
        } else{
            //initHolidayData()
            goToWork()
            //NetworkUtils.networkState.postValue(LoadingState(Status.FAILED, NetworkUtils.ERROR_NO_CONNECTION))
        }

    }

    private fun initHolidayData(){
        val simpleRequest = OneTimeWorkRequestBuilder<HolidayWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 2, TimeUnit.SECONDS)
            .build()
        workManager.enqueueUniqueWork(WORK_SAVE_NAME, ExistingWorkPolicy.KEEP, simpleRequest)
    }

    private fun goToWork(){
        val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }
}
