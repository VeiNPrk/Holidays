package com.vnprk.holidays.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.vnprk.holidays.utils.DateUtils
import java.util.*

@Entity
class PrivateEvent(
    @SerializedName("name")
    override var name: String?,
    @SerializedName("description")
    override var descript: String?,
    @SerializedName("img_link")
    override var img: String?,
    @SerializedName("type")
    override var type: Int,
    @SerializedName("start_date_time")
    var startDateTime: Long,
    @SerializedName("finish_date_time")
    var finishDateTime: Long,
    @SerializedName("period")
    var period: Int,
    @SerializedName("notify_before")
    var notifyBefore: Int,
    @SerializedName("id_color")
    var idColor: Int,
    @SerializedName("is_active")
    var isActive:Boolean = true
):Event{
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0

    fun getStrStartDate() = DateUtils.getStrDateFromMils(startDateTime)

    fun getStrStartTime() = DateUtils.getStrTimeFromMils(startDateTime)

    fun getStrFinishDate() = DateUtils.getStrDateFromMils(finishDateTime)

    fun getStrFinishTime() = DateUtils.getStrTimeFromMils(finishDateTime)

    fun getFullStrFinishDateTime()=
        if(getStrStartDate()==getStrFinishDate()) getStrFinishTime()
        else "${getStrFinishDate()} ${getStrFinishTime()}"

}