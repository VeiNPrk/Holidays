package com.vnprk.holidays.views

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vnprk.holidays.App
import com.vnprk.holidays.R
import com.vnprk.holidays.Repository
import com.vnprk.holidays.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {
    @Inject
    lateinit var repository : Repository
    val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, key: String ->
            if (key.equals("setting_inner_repeats"))
                updateListPrefSummary()
            if (key.equals("setting_notify_holiday_types"))
                updateMultiListPrefSummary(true)
            if (key.equals("setting_notify_holiday_time"))
                updateTimePrefSummary(true)
        }

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       App.instance.appComponent.inject(this)
   }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        updateListPrefSummary()
        updateMultiListPrefSummary(false)
        updateTimePrefSummary(false)
        setHasOptionsMenu(true)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is TimePreference) {
            val f: DialogFragment
            f = TimePreferenceDialogFragment.newInstance(preference.getKey())
            f.setTargetFragment(this, 0)
            fragmentManager?.let { f.show(it, null) }
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_menu_date).isVisible=false
    }

    // Apply for ListPreference with key="pref_style"
    private fun updateListPrefSummary() {
        val preference:ListPreference? = findPreference("setting_inner_repeats")
        val entry = preference?.entry
        preference?.summary = "${context?.getString(R.string.setting_inner_repeat_summary)} $entry"
    }

    private fun updateTimePrefSummary(isChanged:Boolean){
        val preference:TimePreference? = findPreference("setting_notify_holiday_time")
        val entry = preference?.getTime()?:0
        preference?.summary = if(entry > 0){
                if(isChanged) setTime(entry)
                "${context?.getString(R.string.setting_notify_holiday_time_summary)} ${DateUtils.getStrTimeFromMils(entry)}"
            }
            else "${context?.getString(R.string.setting_notify_holiday_time_summary_def)}"
    }

    private fun updateMultiListPrefSummary(isChanged:Boolean) {
        val preference: MultiSelectListPreference? = findPreference("setting_notify_holiday_types")
        val selectedValues = preference?.values
        val selectedTitle = mutableListOf<String>()
        val listTypes = mutableListOf<Int>()
        val preferenceTime: TimePreference? = findPreference("setting_notify_holiday_time")
        val time = preferenceTime?.getTime() ?: 0
        /*val test = SharedPreferencesUtils.getHolidayTipes(requireContext())
        val t = SharedPreferencesUtils.getNotifyHolidayTime(requireContext())*/
        selectedValues?.forEach {
            selectedTitle.add(
                preference?.entries?.get(preference?.entryValues?.indexOf(it) ?: 0).toString()
            )
            listTypes.add(it.toInt())
        }

        preference?.summary = if (selectedTitle.isNullOrEmpty()) {
            findPreference<TimePreference>("setting_notify_holiday_time")?.isEnabled = false
            "${context?.getString(R.string.setting_notify_holiday_types_summary_def)}"
        } else {
            findPreference<TimePreference>("setting_notify_holiday_time")?.isEnabled = true
            "${context?.getString(R.string.setting_notify_holiday_types_summary)} ${selectedTitle.joinToString()}"
        }
        if (isChanged) {
            context?.let { context ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val listHoliday = repository.getHolidayByType(listTypes)
                    val activeAlarmHoliday = repository.getHolidayActiveAlarm()
                    listHoliday.forEach { holiday ->
                        holiday.isAlarm = true
                    }
                    AlarmUtils.updateHolidayAlarm(
                        context,
                        activeAlarmHoliday,
                        listHoliday,
                        time
                    )
                    repository.updateAlarmHolidays(listHoliday)
                }
            }
        }
    }

    private fun setTime(time:Long){
        context?.let{
            lifecycleScope.launch(Dispatchers.IO) {
                val listHoliday = repository.getHolidayActiveAlarm()
                for (holiday in listHoliday){
                    AlarmUtils.setAlarm(it,
                        holiday!!.name?:it.resources.getString(R.string.tv_event_name_null),
                        DateUtils.getAlarmDateTimeForHoliday(holiday, time, false),
                        holiday!!.id,
                        holiday!!.type
                    )
                }
            }
        }
    }
}
