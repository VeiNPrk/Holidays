package com.vnprk.holidays.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils() {
    companion object {

        fun getYears(nYears: Int=100, startYear:Int?=0, finishYear:Int?=0): List<Int> {
            val c = Calendar.getInstance()
            var nowYear = c.get(Calendar.YEAR)
            val finishY = if(finishYear !in 1 until nowYear) nowYear else finishYear
            val startY = if(startYear!! <= 1900) nowYear - nYears else startYear
            //val finishYear = nowYear - nYears
            /*val rangeYears = finishYear.downTo(startYear)

            val years = rangeYears.toList()*/
            //val rangeYears = finishYear.downTo(startYear)
            val years = finishY!!.downTo(startY).toList()

            return years
        }

        fun getDateFromMills(milsec: Long) = Date(milsec)

        //fun getDateFromString(strDate: String) = SimpleDateFormat("dd.MM.yyyy").parse(strDate)

        fun getDateFromString(strDate: String, strFormat:String): Date {
            //SimpleDateFormat("dd.MM.yyyy").parse(strDate)
            val dateFormat = SimpleDateFormat(strFormat, Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormat.parse(strDate)
        }

        /*fun getDateTimeFromString(strDate: String): Date {
            //SimpleDateFormat("dd.MM.yyyy").parse(strDate)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormat.parse(strDate)
        }*/

        fun getSimpleDateStr(date: Date) :String {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormat.format(date)
        } //SimpleDateFormat("dd.MM.yyyy").format(date)

        fun getSimpleDateTimeStr(date: Date) :String {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            //dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            //String.format()
            return dateFormat.format(date)
        } //= SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date)

        fun is24HourInterval(dateCreateMills:Long):Boolean{
            //val nowTimeMills = Calendar.getInstance().time.time
            val nowTimeMills = getDateFromString(getSimpleDateTimeStr(Calendar.getInstance().time), "dd.MM.yyyy HH:mm:ss").time
            val raznMills = nowTimeMills - dateCreateMills
            val hours = raznMills/(1000 * 60 * 60)
            return hours<24
        }

    }
}