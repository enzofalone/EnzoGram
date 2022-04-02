package com.efalone.enzogram

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.efalone.enzogram.fragments.ComposeFragment
import com.efalone.enzogram.fragments.FeedFragment
import com.efalone.enzogram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * Let user create a post by taking a photo with their camera
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentFeed = FeedFragment()
        val fragmentCompose = ComposeFragment()
        val fragmentProfile = ProfileFragment()

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->

            var fragmentToShow: Fragment? = null

            //check which item was selected within the bottom nav view
            when (item.itemId) {
                R.id.actionHome -> {
                    fragmentToShow = fragmentFeed
                }
                R.id.actionCompose -> {
                    fragmentToShow = fragmentCompose
                }
                R.id.actionProfile -> {
                    fragmentToShow = fragmentProfile
                }
            }

            //check if null. if not, go to fragment
            if(fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            //return true to say that we handled this user interaction
            true
        }
        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.actionHome
    }

    // Menu icons are inflated just as they were with actionbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    companion object {
        const val TAG = "MainActivity"
    }
}