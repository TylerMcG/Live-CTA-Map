package com.McGregor.chicagotraintracker.UI;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.McGregor.chicagotraintracker.MainActivity;
import com.McGregor.chicagotraintracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.data.kml.KmlLayer;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import data.Train;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private  final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "MAP_FRAG";
    private GoogleMap map;
    float ZOOM_LEVEL = 11.1f;
    LatLng CHICAGO_LATLNG = new LatLng(41.86638281894, -87.70);
    private ArrayList<Marker> redLineMarkers;
    private ArrayList<Marker> blueLineMarkers;
    private ArrayList<Marker> pinkLineMarkers;
    private ArrayList<Marker> purpleLineMarkers;
    private ArrayList<Marker> orangeLineMarkers;
    private ArrayList<Marker> yellowLineMarkers;
    private ArrayList<Marker> brownLineMarkers;
    private ArrayList<Marker> greenLineMarkers;

    private static boolean isAddedToMap;
    private MarkerInfoWindowAdapter markerInfoWindowAdapter;
    private ValueEventListener redLineListener;
    private ValueEventListener blueLineListener;
    private ValueEventListener brownLineListener;
    private ValueEventListener greenLineListener;
    private ValueEventListener orangeLineListener;
    private ValueEventListener pinkLineListener;
    private ValueEventListener purpleLineListener;
    private ValueEventListener yellowLineListener;
    private  static KmlLayer stops;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, result) -> {
                Log.d(TAG, result.toString());
                updateTrainFromBundle(result);
            });

        } catch (Error e) {
            Log.d(TAG, e.getMessage());
        }
        redLineMarkers = new ArrayList<>();
        blueLineMarkers = new ArrayList<>();
        brownLineMarkers = new ArrayList<>();
        greenLineMarkers = new ArrayList<>();
        orangeLineMarkers = new ArrayList<>();
        purpleLineMarkers = new ArrayList<>();
        pinkLineMarkers = new ArrayList<>();
        yellowLineMarkers = new ArrayList<>();

        redLineListener = initValueEventListener(redLineMarkers, R.drawable.ic_redline);
        blueLineListener =  initValueEventListener(blueLineMarkers, R.drawable.ic_blueline);
        brownLineListener = initValueEventListener(brownLineMarkers, R.drawable.ic_brownline);
        greenLineListener = initValueEventListener(greenLineMarkers, R.drawable.ic_greenline);
        orangeLineListener = initValueEventListener(orangeLineMarkers, R.drawable.ic_orangeline);
        pinkLineListener = initValueEventListener(pinkLineMarkers, R.drawable.ic_pinkline);
        purpleLineListener = initValueEventListener(purpleLineMarkers, R.drawable.ic_purpleline);
        yellowLineListener = initValueEventListener(yellowLineMarkers, R.drawable.ic_yellowline);
    }

    private void handleUpdateTrain(boolean isChecked, String trainLine,
                               ArrayList<Marker> trainMarkers, ValueEventListener trainListener) {
        if (isChecked) {
            updateTrainDataToScreen(trainLine, trainListener);
        } else {
            removeTrainMarkers(trainMarkers);
            dbRef.child(trainLine).removeEventListener(trainListener);
        }
    }

    private void updateTrainFromBundle(Bundle bundle) {
        for (String key: bundle.keySet()) {
            boolean isChecked = bundle.getBoolean(key);
            switch (key) {
                case "Red Line":
                   handleUpdateTrain(isChecked, key, redLineMarkers, redLineListener);
                    break;
                case "Blue Line":
                  handleUpdateTrain(isChecked, key, blueLineMarkers, blueLineListener);
                    break;
                case "Brown Line":
                    handleUpdateTrain(isChecked, key, brownLineMarkers, brownLineListener);
                    break;
                case "Green Line":
                    handleUpdateTrain(isChecked, key, greenLineMarkers, greenLineListener);
                    break;
                case "Orange Line":
                    handleUpdateTrain(isChecked, key, orangeLineMarkers, orangeLineListener);
                    break;
                case "Pink Line":
                  handleUpdateTrain(isChecked, key, pinkLineMarkers, pinkLineListener);
                    break;
                case "Purple Line":
                  handleUpdateTrain(isChecked, key, purpleLineMarkers, purpleLineListener);
                    break;
                case "Yellow Line":
                  handleUpdateTrain(isChecked, key, yellowLineMarkers, yellowLineListener);
                    break;
                default:
                    Log.d(TAG, "Default switch ERROR");
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //load map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.bottomNavigationView.findViewById(R.id.trainsFragment).setEnabled(true);
        mainActivity.bottomNavigationView.findViewById(R.id.stations).setEnabled(true);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "MAP READY");
        map = googleMap;
        try { //add raw kml data to overlay train lines on map
            KmlLayer lines = new KmlLayer(map, R.raw.cta_rail_layer, requireContext());
            stops = new KmlLayer(map, R.raw.cta_stops, requireContext());
            lines.addLayerToMap();
            markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getContext());
            map.setInfoWindowAdapter(markerInfoWindowAdapter);
            stops = new KmlLayer(map, R.raw.cta_stops, requireContext());
        } catch (XmlPullParserException | IOException | Error e) {
            e.printStackTrace();
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CHICAGO_LATLNG, ZOOM_LEVEL));
    }


    private ValueEventListener initValueEventListener(ArrayList<Marker> markers, int resId) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Train> trainData = new ArrayList<>();
                try {
                    //remove previous markers
                    removeTrainMarkers( markers );
                    //populate array with train data
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Train train = ds.getValue(Train.class);
                        trainData.add(train);
                        if(train != null ) {
                            Log.d(TAG, train.toString());
                        }
                    }
                    addTrainMarkers(trainData,
                            resId,
                            markers);

                } catch(DatabaseException | NullPointerException | Error e) {
                    Log.d(TAG , e.getMessage());
                } finally { //clear trainData to hopefully send it to GC
                    trainData.clear();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        };
    }

    private void updateTrainDataToScreen(String trainLine, ValueEventListener eventListener){
        dbRef.child(trainLine).addValueEventListener(eventListener);
    }
    private static void removeTrainMarkers(ArrayList<Marker> trainMarker){
        try{
            for(Marker trainM : trainMarker){
                trainM.remove();
            }
            trainMarker.clear();
        } catch (Error | NullPointerException e) {
            Log.d(TAG, e.getMessage());
        }
    }
    private void addTrainMarkers(ArrayList<Train> trainLine, int trainColorIconResId,
                                 ArrayList<Marker> trainMarkers) {
        try {
            for (Train train : trainLine) {
                String isDelay = Train.formatDelay(train.getIsDly());
                String isApp = Train.formatApp(train.getIsApp());
                String arrivalTime = Train.formatTime(train.getArrT());
                String snippet = ("Train Line: " + train.getLine() + "\n" +
                        "Next Stop: " + train.getNextStaNm() + "\n" +
                        "Dest: " + train.getDestNm() + "\n" +
                        "Delays: " + isDelay + "\n" +
                        "Arrival Time: " + arrivalTime
                        + "\n" + isApp
                );
                Bitmap bitmap = Utils.CreateBitmap.createBitmap(getContext(), trainColorIconResId);
                MarkerOptions m1 = new MarkerOptions()
                        .position(train.generateLatLng())
                        .rotation(train.getHeading())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .title("Train Info")
                        .snippet(snippet);
                Marker onMap = map.addMarker(m1);
                map.setInfoWindowAdapter(markerInfoWindowAdapter);
                trainMarkers.add(onMap);
            }
        } catch (NullPointerException e){
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    public static void toggleStops() {
        try {
            if(stops != null) {
                if(!isAddedToMap) {
                    Log.d(TAG, "stops added");
                    stops.addLayerToMap();
                    isAddedToMap = true;
                }
                else {
                    stops.removeLayerFromMap();
                    isAddedToMap = false;
                    Log.d(TAG, "stops removed");
                }
            }
        } catch (NullPointerException | Error e) {
            Log.d(TAG, e.getMessage() + "Toggle Error");
        }
    }

    @Override
    public void onPause() {
        //need to remove listeners to free up resources
        Log.d(TAG, "ON PAUSE");
        super.onPause();
    }
}