package Utils

import android.util.Log
import java.lang.NullPointerException

import com.McGregor.chicagotraintracker.R
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.McGregor.chicagotraintracker.UI.MapFragment
import java.lang.IllegalStateException
import java.util.*

object FragmentUtilityLoader {
    private const val TAG = "FRAGMENT_LOADER"
    private var transaction: FragmentTransaction? = null
    private const val HOME_FRAG_TAG = "HOME_FRAG_TAG"
    private const val ALERT_FRAG_TAG = "ALERT_FRAG_TAG"
    private fun getTransaction(activity: Activity): FragmentTransaction {
        return getFragmentManager(activity).beginTransaction()
    }

    private fun getFragmentManager(activity: Activity): FragmentManager {
        return (activity as AppCompatActivity).supportFragmentManager
    }

    fun toggleFragmentVisibility(activity: Activity, fragment: Fragment?, id: Int, fragTag: String?) {
        try {
            transaction = getTransaction(activity)
            val currentFragment = getFragmentManager(activity).findFragmentByTag(fragTag)
            val mapFragment = getFragmentManager(activity).findFragmentByTag(HOME_FRAG_TAG)
            if (currentFragment == null) {
                Log.d(TAG, "Fragment added init")
                transaction!!.add(id, fragment!!, fragTag).commit()
            } //alert tree - if current is not visible and already added, and the map is added, and visible
            else if (!currentFragment.isVisible && Objects.requireNonNull(mapFragment)!!.isAdded && mapFragment!!.isVisible) {
                transaction!!.show(fragment!!).commit()
                Log.d(TAG, "Fragment shown: " + currentFragment.isVisible)
            } else if (currentFragment.isVisible && Objects.requireNonNull(mapFragment)!!.isAdded && mapFragment!!.isVisible) {
                transaction!!.hide(fragment!!).commit()
                Log.d(TAG, "Fragment hidden: " + currentFragment.isVisible)
            }
        } catch (e: NullPointerException) {
            Log.d(TAG, "Empty Fragment message: " + e.message)
        } catch (e: IllegalStateException) {
            Log.d(TAG, "Fragment already added: " + e.message)
        }
    }

    fun swapFragments(activity: Activity, mapFrag: MapFragment?, alertFrag: Fragment?) {
        transaction = getTransaction(activity)
        if (alertFrag!!.isVisible) {
            mapFrag!!.onResume()
            transaction!!.setReorderingAllowed(true).hide(alertFrag).show(mapFrag).commit()
        }
    }

    fun swapFragments(activity: Activity, mapFrag: MapFragment?, alertFrag: Fragment?, trainsFrag: Fragment?) {
        transaction = getTransaction(activity)
        if (trainsFrag!!.isVisible) {
            transaction!!.setReorderingAllowed(true).hide(mapFrag!!).hide(trainsFrag).show(alertFrag!!).commit()
        } else {
            transaction!!.setReorderingAllowed(true).hide(mapFrag!!).show(alertFrag!!).commit()
        }
    }

    fun addAndSwapFragments(activity: Activity, alertFrag: Fragment?, mapFrag: MapFragment?) {
        transaction = getTransaction(activity)
        transaction!!.setReorderingAllowed(true).hide(mapFrag!!)
                .add(R.id.fragmentContainerView, alertFrag!!, ALERT_FRAG_TAG)
                .commit()
    }
}