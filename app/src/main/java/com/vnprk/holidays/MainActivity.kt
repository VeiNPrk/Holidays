package com.vnprk.holidays

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.idescout.sql.SqlScoutServer
import com.vnprk.holidays.utils.DatePickerDlg
import com.vnprk.holidays.utils.DateUtils
import com.vnprk.holidays.utils.DeviceRebootReceiver
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), DatePickerDlg.OnDateCompleteListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var isTablet: Boolean = false
    @Inject
    lateinit var repository : Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.appComponent.inject(this)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        isTablet = resources.getBoolean(R.bool.isTablet)
        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        if (drawerLayout != null) {
            appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        } else {
            appBarConfiguration = AppBarConfiguration(navController.graph)
        }
        val receiver = ComponentName(applicationContext, DeviceRebootReceiver::class.java)

        applicationContext.packageManager?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        SqlScoutServer.create(this, getPackageName());
        //toolbar.setS
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id!=R.id.privateEventEditFragment && destination.id!=R.id.nav_fragment_settings)
                toolbar.title = "${navController.currentDestination?.label} ${DateUtils.getStrNameDate(this, repository.getNowDateLong().value?:0)}"
        }
        repository.getNowDateLong().observe(this, Observer { nowDate ->
            toolbar.title = "${navController.currentDestination?.label} ${DateUtils.getStrNameDate(this, nowDate)}"
        })
        //isAppBlacklisted()
    }

    private fun isAppBlacklisted() {
        //val pwrm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = packageName

        val pm =
            getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!pm.isIgnoringBatteryOptimizations(packageName)) intent.action =
                Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            else {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
            /*if(isXiaomi())
                goXiaomiSetting()*/
        }
        /*return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            !pwrm.isIgnoringBatteryOptimizations(name)
        } else {
            false
        }*/
    }

    public fun isXiaomi() :Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }

    private fun goXiaomiSetting() {
        showActivity("com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    private fun showActivity(
        packageName: String,
        activityDir: String
    ) {
        val intent = Intent()
        intent.component = ComponentName(packageName, activityDir)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem)=
        when (item.itemId) {
        R.id.nav_fragment_settings -> {
            val navController = findNavController(R.id.nav_host_fragment)
            item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
        R.id.action_menu_date -> {
            getDateDialog(this).show(supportFragmentManager, "datePicker")
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun getDateDialog(listener: DatePickerDlg.OnDateCompleteListener): DatePickerDlg {
        val dlg = DatePickerDlg.newInstance(repository.nowDate.value?:0, 0)
        dlg.setListener(listener)
        return dlg
    }

    override fun onDateComplete(time: Calendar, viewCode: Int) {
        repository.setNowDate(time.timeInMillis)
        //title="1234"
    }
}
