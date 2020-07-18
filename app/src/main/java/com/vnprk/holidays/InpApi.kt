package com.vnprk.holidays

import com.vnprk.holidays.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface InpApi {
/*	@FormUrlEncoded
	@POST("/service/set_phone_number.php")
	fun sendNumber(@Field("phone") phoneNumber:String, @Field("token") token:String): Call<ResponseClass>

	@FormUrlEncoded
	@POST("/service/authorization_user.php")
	suspend fun sendAuthCode(@Field("code") authCode:String, @Field("token") token:String): Response<UserClass>
*/
	@GET("get_holidays.php")
	suspend fun getHolidays(): Response<ResponseData<Holiday>>

	/*@GET("get_holidays.php")
	suspend fun getHolidays(): Response<String>*/

	@GET("get_holiday_db_version.php")
	suspend fun getDbVersion(): Response<ResponseData<Holiday>>
/*
	@FormUrlEncoded
	@POST("/service/authorization.php")
	fun authorization(@Field("login") login:String, @Field("password") pasw:String, @Field("token") token:String): Call<ResponseClass>

	@FormUrlEncoded
	@POST("/service/check_authorization.php")
	suspend fun sendAuthCode(@Field("code") authCode:String, @Field("token") token:String): Response<UserClass>

	@FormUrlEncoded
	@POST("/service/insert_cast_detail.php")
	suspend fun insertDetail(@Field("detail") detailJson:String): Response<ResponseData<DetailNum>>

	@GET("/service/get_init_data.php")
	suspend fun getInitData(@Query("id_ent") idEnt:Int): Response<ResponseInitData>

	@GET("/service/get_search_data.php")
	suspend fun getSearchNumDetails(@Query("type") type:Int, @Query("id_ent") idEnt:Int, @Query("search") searchString:String): Response<ResponseData<DetailNum>>

	@GET("/service/get_search_data.php")
	suspend fun getSearchNotNumDetails(@Query("type") type:Int, @Query("id_ent") idEnt:Int, @Query("search") searchString:String): Response<ResponseData<DetailNotNum>>
	//fun getInitData(@Query("id_ent") idEnt:Int): Call<String>

	@GET("/service/get_control_details.php")
	fun getControlData(@Query("id_user") idUser:Int, @Query("id_ent") idEnt:Int): Response<ResponseData<DetailNum>>

	@FormUrlEncoded
	@POST("/service/sync_cast_data.php")
	suspend fun synchronizeCastData(@Field("id_user") idUser:Int, @Field("id_ent") idEnt:Int, @Field("details") detailsJson:String): Response<ResponseData<DetailNum>>

	@FormUrlEncoded
	@POST("/service/sync_notnum_data.php")
	suspend fun synchronizeNotNumData(@Field("id_user") idUser:Int, @Field("id_ent") idEnt:Int, @Field("details") detailsJson:String): Response<ResponseData<DetailNotNum>>
*/
}