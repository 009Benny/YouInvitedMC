package com.example.youinvited

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.example.youinvited.ui.showQRPlace.ShowPlaceAtivity
import com.google.zxing.integration.android.IntentIntegrator
import org.intellij.lang.annotations.Language
import java.util.*

class PrincipalActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var navController:NavController? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        val admin = intent.getBooleanExtra("admin", false)
//        this.loadLocate()
        if (admin == true){
            setTheme(R.style.AdminTheme)
        }
        setContentView(R.layout.activity_principal)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        if (admin == true){
            val adminColor: Long = 0xFFCD6155;
            toolbar.setBackgroundColor(adminColor.toInt())
        }
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        if (admin == true){
            val adminColor: Long = 0xFFCD6155;
        }
        this.navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_slideshow, R.id.nav_profile, R.id.nav_listEvents), drawerLayout)
        setupActionBarWithNavController(this.navController!!, appBarConfiguration)
        navView.setupWithNavController(this.navController!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if(result.contents == null){
                Toast.makeText(this, "No se reconocio el código", Toast.LENGTH_LONG)
            }else{
                val intent = Intent(this, ShowPlaceAtivity::class.java).apply {
                    putExtra("qr_code", result.contents.toString())
                }
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.principal, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun changeLanguage(){
        val list = arrayOf("Español", "English")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elige tu lenguaje")
        builder.setSingleChoiceItems(list, -1){ dialog, which ->
            if (which == 0){
                this.setLocale("es")
                recreate()
            }else if (which == 1){
                this.setLocale("en")
                recreate()
            }
            dialog.dismiss()
        }
        builder.create().show()
    }

    fun setLocale(language:String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }

    fun loadLocate(){
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        var language: String? = sharedPreferences.getString("My_Lang", "")
        if (language != null){
            setLocale(language!!)
        }
    }

}