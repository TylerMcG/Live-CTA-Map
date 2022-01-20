package com.McGregor.chicagotraintracker.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.McGregor.chicagotraintracker.R;
import java.util.ArrayList;
import java.util.Arrays;
import Utils.RecyclerViewAdapter;

public class TrainsFragment extends Fragment {

    private final String TAG = "TRAINFRAG";

    private final ArrayList<String> trainNames =
            new ArrayList<>(Arrays.asList("Red Line", "Blue Line", "Brown Line",
                    "Green Line", "Orange Line", "Pink Line", "Purple Line", "Yellow Line"));
    private final ArrayList<Integer> trainIconResID =
            new ArrayList<>(Arrays.asList(R.drawable.ic_redline, R.drawable.ic_blueline,
                    R.drawable.ic_brownline, R.drawable.ic_greenline, R.drawable.ic_orangeline ,
                    R.drawable.ic_pinkline, R.drawable.ic_purpleline, R.drawable.ic_yellowline));

    public TrainsFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train, container, false);
        initRecylerView(view);
        return view;

    }

    @Override
    public void onResume() {
        Log.d(TAG, "resumed");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "paused");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "destroyed");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "detatched");
        super.onDetach();
    }

    private void initRecylerView( View view) {
        try {
            RecyclerView recyclerView = view.findViewById(R.id.recyler_view);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter( this, trainNames, trainIconResID);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } catch (Error e) {
            Log.d("TAG", e.getMessage());
        }

    }
}
