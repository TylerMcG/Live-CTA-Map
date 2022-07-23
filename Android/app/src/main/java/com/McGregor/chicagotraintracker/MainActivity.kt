package com.McGregor.chicagotraintracker
import android.annotation.SuppressLint

import android.util.Log
import android.os.Bundle
import com.McGregor.chicagotraintracker.UI.AlertFragment
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.McGregor.chicagotraintracker.UI.TrainsFragment
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.AuthResult
import android.view.MenuItem
import Utils.FragmentUtilityLoader
import androidx.fragment.app.Fragment
import com.McGregor.chicagotraintracker.UI.MapFragment
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    var bottomNavigationView: BottomNavigationView? = null
    private var mapFrag: MapFragment? = null
    private var alertFrag: Fragment? = null
    private var trainsFrag: Fragment? = null
    var currentUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapFrag = MapFragment()
        alertFrag = AlertFragment()
        trainsFrag = TrainsFragment()
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView?.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        bottomNavigationView?.setOnItemSelectedListener(navigationListener)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        //if current user is null, sign them in anonymously
        if (currentUser == null) {
            mAuth!!.signInAnonymously().addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Signed in Anon")
                    currentUser = mAuth!!.currentUser
                } else {
                    if (task.exception != null) {
                        Log.d(TAG, task.exception.toString())
                    }
                }
            }
        }
        //user is not null and signed in anonymously as intended, testing purposes for now
        if (currentUser != null && currentUser!!.isAnonymous) {
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).add(R.id.fragmentContainerView, mapFrag!!, HOME_FRAG_TAG).commit()
        }
    }

    //method needs to be cleaned up and abstracted to FragmentUtilityLoader
    @SuppressLint("NonConstantResourceId")
    private val navigationListener = NavigationBarView.OnItemSelectedListener { menuItem: MenuItem ->
        when (menuItem.itemId) {
            R.id.mapFragment -> {
                mapFrag!!.onResume()
                FragmentUtilityLoader.swapFragments(this, mapFrag, alertFrag)
                return@OnItemSelectedListener true
            }
            R.id.trainsFragment -> {
                FragmentUtilityLoader.toggleFragmentVisibility(this, trainsFrag, R.id.fragmentContainerView, TRAIN_FRAG_TAG)
                return@OnItemSelectedListener true
            }
            R.id.stations -> {
                mapFrag!!.toggleStops()
                return@OnItemSelectedListener true
            }
            R.id.alertFragment -> {
                mapFrag!!.onPause()
                if (!alertFrag!!.isAdded) {
                    FragmentUtilityLoader.addAndSwapFragments(this, alertFrag, mapFrag)
                } else if (mapFrag!!.isVisible) {
                    FragmentUtilityLoader.swapFragments(this, mapFrag, alertFrag, trainsFrag)
                }
                return@OnItemSelectedListener true
            }
        }
        return@OnItemSelectedListener false
    }

    override fun onBackPressed() {
        val alertTestFrag = supportFragmentManager.findFragmentByTag(ALERT_FRAG_TAG)
        if (alertTestFrag is AlertFragment) {
            alertTestFrag.handleGoBack()
        }
    }

    companion object {
        private const val TRAIN_FRAG_TAG = "TRAIN_FRAG_TAG"
        private const val HOME_FRAG_TAG = "HOME_FRAG_TAG"
        private const val TAG = "MainActivity"
        private const val ALERT_FRAG_TAG = "ALERT_FRAG_TAG"
    }
}