package com.vnprk.holidays.utils

import android.content.Context
import com.vnprk.holidays.R
import com.vnprk.holidays.models.Event
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

        fun getDayFromMills(millsTime:Long):Int{
            val cal = Calendar.getInstance()
            cal.timeInMillis=millsTime
            return cal.get(Calendar.DAY_OF_MONTH)
        }

        fun getMonthFromMills(millsTime:Long):Int{
            val cal = Calendar.getInstance()
            cal.timeInMillis=millsTime
            return cal.get(Calendar.MONTH)+1
        }

        fun getDayOfYearFromMills(millsTime:Long):Int{
            val cal = Calendar.getInstance()
            cal.timeInMillis=millsTime
            return cal.get(Calendar.DAY_OF_YEAR)
        }

        fun getNowDateTimeMills() = Calendar.getInstance().timeInMillis

        fun getDateFromString(strDate: String, strFormat:String): Date {
            //SimpleDateFormat("dd.MM.yyyy").parse(strDate)
            val dateFormat = SimpleDateFormat(strFormat, Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormat.parse(strDate)
        }

        fun getStrDateFromMils(time:Long) :String{
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return "${calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2,'0')}.${(calendar.get(Calendar.MONTH)+1).toString().padStart(2,'0')}.${calendar.get(Calendar.YEAR)}"
        }

        fun getStrTimeFromMils(time:Long):String{
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return "${calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2,'0')}:${calendar.get(Calendar.MINUTE).toString().padStart(2,'0')}"
        }

        fun getMilsWithAdding(time :Long, adding:Int):Long {
            val calendar = Calendar.getInstance()
            val calendarNow = Calendar.getInstance()
            calendar.timeInMillis = time
            val addMin = Event.notifysMin.get(adding)
            addMin?.let {min->
                if(min<40000)
                    calendar.add(Calendar.MINUTE, -min)
                else
                    calendar.add(Calendar.MONTH, -1)
            }
            if(calendar.timeInMillis < calendarNow.timeInMillis)
            {
                calendar.timeInMillis=calendarNow.timeInMillis
                calendar.add(Calendar.SECOND, 30)
            }
            return calendar.timeInMillis
        }

        fun getStrNameDate(context: Context, time:Long):String{
            val calendarNow = Calendar.getInstance()
            val calendarDate = Calendar.getInstance()
            if(time>0)
                calendarDate.timeInMillis=time
            val nowMonth = calendarNow.get(Calendar.MONTH)
            val dateMonth = calendarDate.get(Calendar.MONTH)
            val nowDay = calendarNow.get(Calendar.DAY_OF_MONTH)
            val dateDay = calendarDate.get(Calendar.DAY_OF_MONTH)
            var strDate = ""
            val months = context.resources.getStringArray(R.array.arr_months)
            if(nowMonth==dateMonth && nowDay==dateDay){
                strDate="${context.resources.getString(R.string.bar_today)} $dateDay ${months[dateMonth]}"
            }else{
                strDate="$dateDay ${months[dateMonth]}"
            }
            return strDate
        }

        fun getMilsWithAddingPeriod(time :Long, period:Int):Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            when (period){
                1-> calendar.add(Calendar.DAY_OF_MONTH,1)
                2-> calendar.add(Calendar.DAY_OF_MONTH, 7)
                3-> calendar.add(Calendar.MONTH, 1)
                4-> calendar.add(Calendar.YEAR, 1)
            }
            return calendar.timeInMillis
        }

        fun getFullDateFormat(context: Context, startTime:Long, finishTime:Long):String {
            var str=""
            val dayOfWeek = context.resources.getStringArray(R.array.arr_day_of_week)
            val months = context.resources.getStringArray(R.array.arr_shot_months)

            val calendarNow = Calendar.getInstance()
            val calendarStart = Calendar.getInstance()
            calendarStart.timeInMillis = startTime
            val calendarFinish = Calendar.getInstance()
            calendarFinish.timeInMillis = finishTime
            var strTime = "${getStrTimeFromMils(startTime)} - ${getStrTimeFromMils(finishTime)}"
            var startYear = "${calendarStart.get(Calendar.YEAR)}"
            var finishYear = "${calendarFinish.get(Calendar.YEAR)}"
            val startDayOfWeek = calendarStart.get(Calendar.DAY_OF_WEEK)
            val finishDayOfWeek = calendarFinish.get(Calendar.DAY_OF_WEEK)
            val startMon = calendarStart.get(Calendar.MONTH)
            val finishMon = calendarFinish.get(Calendar.MONTH)
            val startDayOfMon = calendarStart.get(Calendar.DAY_OF_MONTH)
            val finishDayOfMon = calendarFinish.get(Calendar.DAY_OF_MONTH)
            val startStrTime = getStrTimeFromMils(startTime)
            val finishStrTime= getStrTimeFromMils(finishTime)

            if(calendarStart.get(Calendar.YEAR)==calendarNow.get(Calendar.YEAR)) {
                startYear=""
            }
            if(calendarFinish.get(Calendar.YEAR)==calendarNow.get(Calendar.YEAR)) {
                finishYear=""
            }
            if(calendarStart.get(Calendar.YEAR)!=calendarFinish.get(Calendar.YEAR)){
                str = "${dayOfWeek[startDayOfWeek]}, $startDayOfMon ${months[startMon]} $startYear, $startStrTime - ${dayOfWeek[finishDayOfWeek]}, $finishDayOfMon ${months[finishMon]} $finishYear, $finishStrTime"
            }else{
                if(calendarFinish.get(Calendar.DAY_OF_YEAR)-calendarStart.get(Calendar.DAY_OF_YEAR)>0){
                    str = "${dayOfWeek[startDayOfWeek]}, $startDayOfMon ${months[startMon]}, $startStrTime - ${dayOfWeek[finishDayOfWeek]}, $finishDayOfMon ${months[finishMon]}, $finishStrTime"
                } else{
                    str = "${dayOfWeek[startDayOfWeek]}, $startDayOfMon ${months[startMon]}, $strTime"
                }
            }
            /*str = if(calendarStart.get(Calendar.YEAR)!=calendarNow.get(Calendar.YEAR)){
                if(calendarFinish.get(Calendar.DAY_OF_YEAR)-calendarStart.get(Calendar.DAY_OF_YEAR)>0)
                    "${dayOfWeek[calendarStart.get(Calendar.DAY_OF_WEEK)]}, "+
                    "${calendarStart.get(Calendar.DAY_OF_MONTH)} "+ "${months[calendarStart.get(Calendar.MONTH)]} "+
                            "${calendarStart.get(Calendar.YEAR)} ${getStrTimeFromMils(startTime)} - "+
                            if(calendarFinish.get(Calendar.YEAR)!=calendarNow.get(Calendar.YEAR))
                                "${dayOfWeek[calendarFinish.get(Calendar.DAY_OF_WEEK)]}, "+
                                "${calendarFinish.get(Calendar.DAY_OF_MONTH)} "+ "${months[calendarFinish.get(Calendar.MONTH)]} "+
                                "${calendarFinish.get(Calendar.YEAR)} ${getStrTimeFromMils(finishTime)}"
                            else
                                "${dayOfWeek[calendarFinish.get(Calendar.DAY_OF_WEEK)]}, "+
                                "${calendarFinish.get(Calendar.DAY_OF_MONTH)} "+ "${months[calendarFinish.get(Calendar.MONTH)]} "+
                                "${getStrTimeFromMils(finishTime)}"
                else
                    if(calendarStart.get(Calendar.YEAR)!=calendarNow.get(Calendar.YEAR))
                        "${dayOfWeek[calendarStart.get(Calendar.DAY_OF_WEEK)]}, ${calendarStart.get(Calendar.DAY_OF_MONTH)} ${months[calendarStart.get(Calendar.MONTH)]} ${calendarStart.get(Calendar.YEAR)} $strTime"
                    else
                        "${dayOfWeek[calendarStart.get(Calendar.DAY_OF_WEEK)]}, ${calendarStart.get(Calendar.DAY_OF_MONTH)} ${months[calendarStart.get(Calendar.MONTH)]} $strTime"


            }else{
                "${dayOfWeek[calendarStart.get(Calendar.DAY_OF_WEEK)]}, ${calendarStart.get(Calendar.DAY_OF_MONTH)} ${months[calendarStart.get(Calendar.MONTH)]} $strTime"
            }*/
            return str
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