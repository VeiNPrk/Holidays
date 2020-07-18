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
    @ColumnInfo(name = "id_holiday")
    @PrimaryKey
    /*@Expose*/
    override var idEvent: Int,
    @SerializedName("name")
    @ColumnInfo(name = "name_holiday")
    /*@Expose*/
    override var nameEvent: String,
    @SerializedName("description")
    @ColumnInfo(name = "description_holiday")
    /*@Expose*/
    override var descriptEvent: String,
    @SerializedName("img_link")
    @ColumnInfo(name = "img_holiday")
    /*@Expose*/
    override var imgEvent: String?,
    @SerializedName("type")
    /*@Expose*/
    var type: Int,
    @SerializedName("country")
    /*@Expose*/
    var country: Int,
    @SerializedName("day")
    /*@Expose*/
    var day: Int?,
    @SerializedName("month")
    /*@Expose*/
    var month: Int?,
    @SerializedName("day_of_year")
    /*@Expose*/
    var dayOfYear: Int?,
    @SerializedName("ordered")
    /*@Expose*/
    var ordered: Int
):Event(idEvent, nameEvent, descriptEvent, imgEvent){

}