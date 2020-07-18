package com.vnprk.holidays.models

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.vnprk.holidays.App
import com.vnprk.holidays.R
import com.vnprk.holidays.Repository
//import com.vnprk.holidays.viewmodels.DetailCastViewModel
import javax.inject.Inject

class HolidayWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var repository: Repository

    override suspend fun doWork(): Result {
        if (applicationContext is App) {
            (applicationContext as App).appComponent.inject(this)
            Log.d("MyWorker", "doWork: start")
            //sendNotification("Simple Work Manager", "I have been send by WorkManager.");
            //val repository = App.instance.getRepository()!!
            /*val userId = inputData.getInt(DetailCastViewModel.KEY_WORK_ID_USER, 0)
            val userEnt = inputData.getInt(DetailCastViewModel.KEY_WORK_ENT_USER, 0)*/
            //sendNotification("Simple Work Manager", "I have been send by WorkManager. $userId $userEnt");
            /*repository!!.syncronizeData(userId, userEnt)*/
            //if (NetworkUtils.isNetworkAvailable(appContext)) {
            //repository!!.insertCastDetailToRemote(it)

            //}
            Log.d("MyWorker", "doWork: end")
            return repository.syncronizeHolidaysData(applicationContext)
        } else
            return ListenableWorker.Result.failure()

        /*val response = checkoutApi
                .resetOrderSynchronous(orderRepository.getOrder())
                .execute()

        if (response.isSuccessful) {
            return Result.SUCCESS
        } else {
            if (response.code() in (500..599)) {
                // try again if there is a server error
                return Result.RETRY
            }
            return Result.FAILURE
        } */
    }

    /*fun sendNotification( title:String,  message:String) {
          val notificationManager = getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
 
        val notification = NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
 
        notificationManager.notify(1, notification.build());
    }*/
}