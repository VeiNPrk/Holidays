package com.vnprk.holidays.utils

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import androidx.preference.DialogPreference

class TimePreference(
    context: Context?,
    attrs: AttributeSet?
) :
    DialogPreference(context, attrs) {

    init{
        dialogTitle=null
    }

    private var mDateValue: Long? = null
    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getString(index)
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        setTime(getPersistedLong("$defaultValue".toLongOrNull()?:0))
    }

    /**
     * Gets the date as a string from the current data storage.
     *
     * @return string representation of the date.
     */
    fun getTime(): Long? {
        return mDateValue
    }

    /**
     * Saves the date as a string in the current data storage.
     *
     * @param text string representation of the date to save.
     */
    fun setTime(timeInMills: Long?) {
        val wasBlocking = shouldDisableDependents()
        mDateValue = timeInMills
        //persistString(text)
        persistLong(timeInMills?:0)
        val isBlocking = shouldDisableDependents()
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking)
        }
        notifyChanged()
    }

    /**
     * A simple [androidx.preference.Preference.SummaryProvider] implementation for an
     * [DatePreference]. If no value has been set, the summary displayed will be 'Not
     * set', otherwise the summary displayed will be the value set for this preference.
     */
    class SimpleSummaryProvider private constructor() : SummaryProvider<TimePreference> {
        override fun provideSummary(preference: TimePreference): CharSequence? {
            return if (preference.getTime()==null/*TextUtils.isEmpty(preference.getTime())*/) {""
                /*preference.context.getString(R.string.not_set)*/
            } else {
                preference.getTime().toString()
            }
        }

        companion object {
            private var sSimpleSummaryProvider: SimpleSummaryProvider? = null

            /**
             * Retrieve a singleton instance of this simple
             * [androidx.preference.Preference.SummaryProvider] implementation.
             *
             * @return a singleton instance of this simple
             * [androidx.preference.Preference.SummaryProvider] implementation
             */
            val instance: SimpleSummaryProvider?
                get() {
                    if (sSimpleSummaryProvider == null) {
                        sSimpleSummaryProvider =
                            TimePreference.SimpleSummaryProvider()
                    }
                    return sSimpleSummaryProvider
                }
        }
    }
}