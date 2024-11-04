package com.example.opsc7213_goalignite

// MainActivity.kt

import android.content.Context
import android.content.Intent



import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
     private lateinit var drawerLayout: DrawerLayout
    private lateinit var inAppReviewHelper: InAppReviewHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme before setting content view
        val sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("nightMode", false)
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseApp.initializeApp(this)

        // In your MainActivity or other hosting Activity
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentTimer())
            .commit()


        // Set up the toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Set up NavigationView (sidebar)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Set up the drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> loadFragment(FragmentHome())
                R.id.bottom_settings-> loadFragment(FragmentSettings())
                R.id.bottom_timer -> loadFragment(FragmentTimer())
                R.id.bottom_calendar -> loadFragment(FragmentCalendar())

            }
            true
        }

        // Load the default fragment when the activity starts
        if (savedInstanceState == null) {
            loadFragment(FragmentHome())
        }
        // Initialize the InAppReviewHelper
        inAppReviewHelper = InAppReviewHelper(this)

        someActionCompleted()
    }


    // Handle NavigationView (sidebar) menu item clicks
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.study -> loadFragment(FragmentStudy())
            R.id.todo -> loadFragment(FragmentList())
            R.id.grades -> loadFragment(FragmentGrades())
            R.id.contact -> loadFragment(FragmentContact())
            R.id.logout -> {
                // Clear the user session if applicable, then navigate to Login
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish() // Optional: Call finish() if you want to close the current activity
            }
        }
        // Close the drawer after selection
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Load the selected fragment into the FrameLayout
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    private fun someActionCompleted() {
        inAppReviewHelper.requestReviewInfo(
            onReviewInfoReceived = { reviewInfo ->
                reviewInfo?.let {
                    // Launch the review flow if ReviewInfo is not null
                    inAppReviewHelper.launchReviewFlow(this, it)
                }
            },
            onError = { errorCode ->
                // Handle error (e.g., log the error)
                errorCode?.let {
                    // Log or show a message based on the error code
                }
            }
        )
    }
    // Handle back press to close the drawer if it's open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
