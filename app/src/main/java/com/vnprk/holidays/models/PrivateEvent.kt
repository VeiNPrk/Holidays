package com.vnprk.holidays.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
class PrivateEvent(
    @SerializedName("id")
    @ColumnInfo(name = "id_private")
    @PrimaryKey
    override var idEvent: Int,
    @SerializedName("name")
    @ColumnInfo(name = "name_private")
    override var nameEvent: String,
    @SerializedName("description")
    @ColumnInfo(name = "description_private")
    override var descriptEvent: String,
    @SerializedName("img_link")
    @ColumnInfo(name = "img_private")
    override var imgEvent: String?,
    @SerializedName("type")
    var type: Int,
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
    @SerializedName("period")
    var period: Int
):Event(idEvent, nameEvent, descriptEvent, imgEvent){

}