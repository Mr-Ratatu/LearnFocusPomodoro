package com.learn.focus.pomodoro.app.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.learn.focus.pomodoro.app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        NavigationUI.setupWithNavController(navigation, navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        findViewById<ImageView>(R.id.navigation_button).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        titleFragment.text = navController.currentDestination?.label
    }

}

