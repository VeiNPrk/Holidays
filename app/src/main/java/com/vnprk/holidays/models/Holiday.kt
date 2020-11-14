package com.vnprk.holidays.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
class Holiday(
    @SerializedName("id")
    /*@ColumnInfo(name = "id_holiday")*/
    @PrimaryKey
    override var id: Int,
    @SerializedName("name")
    /*@ColumnInfo(name = "name_holiday")*/
    override var name: String?,
    @SerializedName("description")
    /*@ColumnInfo(name = "description_holiday")*/
    override var descript: String?,
    @SerializedName("img_link")
    override var img: String?,
    @SerializedName("link")
    var link: String?,
    @SerializedName("type")
    override var type: Int,
    @SerializedName("country")
    var country: Int,
    @SerializedName("day")
    var day: Int?,
    @SerializedName("month")
    var month: Int?,
    @SerializedName("day_of_year")
    var dayOfYear: Int?,
    @SerializedName("ordered")
    var ordered: Int,
    @SerializedName("is_alarm")
    var isAlarm:Boolean = false
):Event/*(/*idEvent, nameEvent, descriptEvent, imgEvent*/)*/{

}