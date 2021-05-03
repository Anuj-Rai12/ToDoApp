package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    /*
    private lateinit var appBarConfiguration: AppBarConfiguration*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*  navController = findNavController(R.id.fragment)
          appBarConfiguration =
              AppBarConfiguration(setOf(R.id.taskFragment, R.id.taskEditAddFragment))
          setupActionBarWithNavController(navController, appBarConfiguration)*/
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.fragment)
        return navController.navigateUp()||super.onSupportNavigateUp()
    }*/
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}