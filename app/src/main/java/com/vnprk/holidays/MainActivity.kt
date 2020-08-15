package com.vnprk.holidays

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.idescout.sql.SqlScoutServer
import com.vnprk.holidays.utils.DatePickerDlg
import com.vnprk.holidays.utils.DateUtils
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
        //setHasOptionsMenu(true)
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
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
        SqlScoutServer.create(this, getPackageName());

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            //if(destination!=)
            if(destination.id!=R.id.privateEventEditFragment)
                /*toolbar.title = navController.currentDestination?.label
            else*/
                toolbar.title = "${navController.currentDestination?.label} ${DateUtils.getStrNameDate(this, repository.getNowDateLong().value?:0)}"
        }
        repository.getNowDateLong().observe(this, Observer { nowDate ->
            //activity?.title=DateUtils.getStrNameDate(requireContext(), nowDate)
            toolbar.title = "${navController.currentDestination?.label} ${DateUtils.getStrNameDate(this, nowDate)}"
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem)=
        when (item.itemId) {
        R.id.menu_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }
        R.id.action_menu_date -> {
            //showDatePickerDialog(0)
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            getDateDialog(this).show(supportFragmentManager, "datePicker")
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
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
        title="1234"
    }
}
