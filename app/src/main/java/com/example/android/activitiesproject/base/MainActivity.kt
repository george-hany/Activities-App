package com.example.android.activitiesproject.base

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.ui.login.LoginActivity
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{


lateinit var pref:SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        MyApplication.injectMainActivity(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        pref=SharedPreferenceHelper(this)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        toolbar.setNavigationIcon(R.drawable.nav_icon)
        navView.itemIconTintList = null
        val userImageOption = RequestOptions().placeholder(R.drawable.user_default_grey)
            .error(R.drawable.user_default_grey)
        Glide.with(this).load(pref.getImage()!!).apply(userImageOption).into(navView.getHeaderView(0).nav_profile_image)
        navView.getHeaderView(0).nav_user_name.text=pref.getUserName()


    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                findNavController(R.id.fragment).navigate(
                    R.id.homeFragment, null,
                    NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                )
            }

            R.id.nav_rank -> {

                findNavController(R.id.fragment).navigate(R.id.rankFragment)
            }
            R.id.nav_profile -> {
                findNavController(R.id.fragment).navigate(R.id.profileFragment)
            }
            R.id.nav_events -> {
                findNavController(R.id.fragment).navigate(R.id.eventsFragment)
            }
            R.id.nav_logout -> {
                pref.logOut()
                val intent= Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
