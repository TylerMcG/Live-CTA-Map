package com.McGregor.chicagotraintracker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import com.McGregor.chicagotraintracker.UI.AlertFragment;
import com.McGregor.chicagotraintracker.UI.MapFragment;
import com.McGregor.chicagotraintracker.UI.TrainsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import Utils.FragmentUtilityLoader;

public class MainActivity extends AppCompatActivity {
    private static final String TRAIN_FRAG_TAG = "TRAIN_FRAG_TAG";
    private static final String HOME_FRAG_TAG = "HOME_FRAG_TAG";
    private static final String TAG = "MainActivity";
    private static final String ALERT_FRAG_TAG = "ALERT_FRAG_TAG";
    private FirebaseAuth mAuth;
    public BottomNavigationView bottomNavigationView;
    private MapFragment mapFrag;
    private Fragment alertFrag;
    private Fragment trainsFrag;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFrag = new MapFragment();
        alertFrag = new AlertFragment();
        trainsFrag = new TrainsFragment();
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setOnItemSelectedListener(navigationListener);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //if current user is null, sign them in anonymously
        if(currentUser == null) {
            mAuth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Signed in Anon");
                    currentUser = mAuth.getCurrentUser();
                } else {
                    if(task.getException() != null) {
                        Log.d(TAG, task.getException().toString());
                    }
                }
            });
        }
        //user is not null and signed in anonymously as intended, testing purposes for now
        if (currentUser != null && currentUser.isAnonymous()) {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragmentContainerView, mapFrag, HOME_FRAG_TAG).commit();
        }

    }
    //method needs to be cleaned up and abstracted to FragmentUtilityLoader
    @SuppressLint("NonConstantResourceId")
    private final NavigationBarView.OnItemSelectedListener  navigationListener = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.mapFragment:
                mapFrag.onResume();
                FragmentUtilityLoader.swapFragments(this, mapFrag, alertFrag);
                return true;
            case R.id.trainsFragment:
                FragmentUtilityLoader.toggleFragmentVisibility(this, trainsFrag, R.id.fragmentContainerView, TRAIN_FRAG_TAG);
                return true;
            case R.id.stations:
                mapFrag.toggleStops();
                return true;
            case R.id.alertFragment:
                mapFrag.onPause();
                if (!alertFrag.isAdded()) {
                    FragmentUtilityLoader.addAndSwapFragments(this, alertFrag, mapFrag);
                }
                else if (mapFrag.isVisible()) {
                   FragmentUtilityLoader.swapFragments(this, mapFrag, alertFrag, trainsFrag);
                }
                return true;
        }
        return false;
    };

    @Override
    public void onBackPressed(){
        Fragment alertTestFrag = getSupportFragmentManager().findFragmentByTag(ALERT_FRAG_TAG);
        if (alertTestFrag instanceof AlertFragment) {
            ((AlertFragment) alertTestFrag).handleGoBack();
        }
    }
}

