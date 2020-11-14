package com.vnprk.holidays.models

class ResponseData<T>(
     val isSucces : Int,
     val message : String,
     val result : List<T>?,
     val versionDb :Int,
     val dateUpdateDb:String
)