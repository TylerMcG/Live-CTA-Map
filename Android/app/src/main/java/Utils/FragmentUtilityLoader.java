package Utils;

import android.app.Activity;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.McGregor.chicagotraintracker.MainActivity;
import com.McGregor.chicagotraintracker.UI.MapFragment;

import java.util.Map;

public class FragmentUtilityLoader {
    private static final String TAG = "FRAGMENT_LOADER";
    private static FragmentTransaction transaction;
    private static final String HOME_FRAG_TAG = "HOME_FRAG_TAG";
    private static final String ALERT_FRAG_TAG = "ALERT_FRAG_TAG";

    private static FragmentTransaction getTransaction(Activity activity) {
        return getFragmentManager(activity).beginTransaction();
    }
    private static FragmentManager getFragmentManager(Activity activity){
        return ((AppCompatActivity)activity).getSupportFragmentManager();
    }


    public static void toggleFragmentVisibility(Activity activity, Fragment fragment, int id, String fragTag){
        try{
            transaction = getTransaction(activity);
            Fragment currentFragment = getFragmentManager(activity).findFragmentByTag(fragTag);
            Fragment mapFragment = getFragmentManager(activity).findFragmentByTag(HOME_FRAG_TAG);
            if (currentFragment == null){
                Log.d(TAG, "Fragment added init");
                transaction.add(id, fragment, fragTag).commit();
            } //alert tree - if current is not visible and already added, and the map is added, and visible
            else if (currentFragment != null && !currentFragment.isVisible() &&
            mapFragment.isAdded() && mapFragment.isVisible() ) {
                transaction.show(fragment).commit();
                Log.d(TAG, "Fragment shown: " + currentFragment.isVisible());
            }
            else if (currentFragment != null && currentFragment.isVisible() &&
                    mapFragment.isAdded() && mapFragment.isVisible()) {
                transaction.hide(fragment).commit();
                Log.d(TAG, "Fragment hidden: " + currentFragment.isVisible());
            }
        } catch(NullPointerException e ) {
            Log.d(TAG, "Empty Fragment message: "+ e.getMessage());
        } catch (IllegalStateException e) {
            Log.d(TAG, "Fragment already added: " + e.getMessage());
        }
    }

}
