package com.McGregor.chicagotraintracker.UI

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList
import com.google.firebase.database.ValueEventListener
import android.location.LocationManager
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import android.annotation.SuppressLint
import java.lang.SecurityException
import android.util.Log
import java.lang.NullPointerException
import android.os.Bundle
import com.McGregor.chicagotraintracker.R
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.maps.SupportMapFragment
import com.McGregor.chicagotraintracker.MainActivity
import com.google.maps.android.data.kml.KmlLayer
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseError
import Utils.CreateBitmap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.Manifest
import android.content.Context
import android.location.LocationListener
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.Marker
import data.Train
import java.lang.Error

class MapFragment : Fragment(), OnMapReadyCallback {
    private val dbRef = FirebaseDatabase.getInstance().reference
    private var map: GoogleMap? = null
    private val CHICAGO_LATLNG = LatLng(41.86638281894, -87.70)
    private var redLineMarkers: ArrayList<Marker?>? = null
    private var blueLineMarkers: ArrayList<Marker?>? = null
    private var pinkLineMarkers: ArrayList<Marker?>? = null
    private var purpleLineMarkers: ArrayList<Marker?>? = null
    private var orangeLineMarkers: ArrayList<Marker?>? = null
    private var yellowLineMarkers: ArrayList<Marker?>? = null
    private var brownLineMarkers: ArrayList<Marker?>? = null
    private var greenLineMarkers: ArrayList<Marker?>? = null
    private var isAddedToMap = false
    private var isTrackingLocation = false
    private var markerInfoWindowAdapter: MarkerInfoWindowAdapter? = null
    private var redLineListener: ValueEventListener? = null
    private var blueLineListener: ValueEventListener? = null
    private var brownLineListener: ValueEventListener? = null
    private var greenLineListener: ValueEventListener? = null
    private var orangeLineListener: ValueEventListener? = null
    private var pinkLineListener: ValueEventListener? = null
    private var purpleLineListener: ValueEventListener? = null
    private var yellowLineListener: ValueEventListener? = null
    private var locationManager: LocationManager? = null
    private var locationPermissionGranted = false
    private var locationButton: ImageButton? = null/*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

    /**
     * Prompts the user for permission to use the device location.
     */
    private val locationPermission: Unit
        private get() {
            /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
            if (ContextCompat.checkSelfPermission(requireContext().applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
        }
    private val locationListener = LocationListener { location ->
        val latLng = LatLng(location.latitude, location.longitude)
        val LOCATION_ZOOM_LEVEL = 15.0f
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, LOCATION_ZOOM_LEVEL))
    }

    @SuppressLint("MissingPermission")
    private fun updateUserLocationOnMap() {
        try {
            if (locationPermissionGranted) {
                locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        } catch (e: NullPointerException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * Shows the user's location on the map if permission is granted
     */
    @SuppressLint("MissingPermission")
    private fun showUserLocation() {
        if (map == null) {
            Log.e(TAG, "Null map")
            return
        }
        try {
            //hide the button since custom button was created
            map!!.uiSettings.isMyLocationButtonEnabled = false
            if (locationPermissionGranted) {
                Log.e(TAG, "granted")
                map!!.isMyLocationEnabled = true
            } else {
                Log.d(TAG, "not granted")
                map!!.isMyLocationEnabled = false
                locationPermission
            }
        } catch (e: SecurityException) {
            Log.e("$TAG Exception: %s", e.message!!)
        } catch (e: Error) {
            Log.e("$TAG Exception: %s", e.message!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            parentFragmentManager.setFragmentResultListener("requestKey", this, { requestKey: String?, result: Bundle ->
                Log.d(TAG, result.toString())
                updateTrainFromBundle(result)
            })
        } catch (e: Error) {
            Log.d(TAG, e.message!!)
        }
        //initialize arraylist of markers for each trainline
        redLineMarkers = ArrayList()
        blueLineMarkers = ArrayList()
        brownLineMarkers = ArrayList()
        greenLineMarkers = ArrayList()
        orangeLineMarkers = ArrayList()
        purpleLineMarkers = ArrayList()
        pinkLineMarkers = ArrayList()
        yellowLineMarkers = ArrayList()
        //initialize listeners
        redLineListener = initValueEventListener(redLineMarkers!!, R.drawable.ic_redline)
        blueLineListener = initValueEventListener(blueLineMarkers!!, R.drawable.ic_blueline)
        brownLineListener = initValueEventListener(brownLineMarkers!!, R.drawable.ic_brownline)
        greenLineListener = initValueEventListener(greenLineMarkers!!, R.drawable.ic_greenline)
        orangeLineListener = initValueEventListener(orangeLineMarkers!!, R.drawable.ic_orangeline)
        pinkLineListener = initValueEventListener(pinkLineMarkers!!, R.drawable.ic_pinkline)
        purpleLineListener = initValueEventListener(purpleLineMarkers!!, R.drawable.ic_purpleline)
        yellowLineListener = initValueEventListener(yellowLineMarkers!!, R.drawable.ic_yellowline)
    }

    private fun handleUpdateTrain(isChecked: Boolean, trainLine: String,
                                  trainMarkers: ArrayList<Marker?>?, trainListener: ValueEventListener?) {
        if (isChecked) {
            updateTrainDataToScreen(trainLine, trainListener)
        } else {
            removeTrainMarkers(trainMarkers)
            dbRef.child(trainLine).removeEventListener(trainListener!!)
        }
    }

    private fun updateTrainFromBundle(bundle: Bundle) {
        for (key in bundle.keySet()) {
            val isChecked = bundle.getBoolean(key)
            when (key) {
                "Red Line" -> handleUpdateTrain(isChecked, key, redLineMarkers, redLineListener)
                "Blue Line" -> handleUpdateTrain(isChecked, key, blueLineMarkers, blueLineListener)
                "Brown Line" -> handleUpdateTrain(isChecked, key, brownLineMarkers, brownLineListener)
                "Green Line" -> handleUpdateTrain(isChecked, key, greenLineMarkers, greenLineListener)
                "Orange Line" -> handleUpdateTrain(isChecked, key, orangeLineMarkers, orangeLineListener)
                "Pink Line" -> handleUpdateTrain(isChecked, key, pinkLineMarkers, pinkLineListener)
                "Purple Line" -> handleUpdateTrain(isChecked, key, purpleLineMarkers, purpleLineListener)
                "Yellow Line" -> handleUpdateTrain(isChecked, key, yellowLineMarkers, yellowLineListener)
                else -> Log.d(TAG, "Default switch ERROR")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        //load map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        val mainActivity = (activity as MainActivity?)!!
        locationButton = view.findViewById(R.id.locationButton)
        mainActivity.bottomNavigationView!!.findViewById<View>(R.id.trainsFragment).isEnabled = true
        mainActivity.bottomNavigationView!!.findViewById<View>(R.id.stations).isEnabled = true
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
            locationButton?.setOnClickListener(View.OnClickListener { buttonView: View ->
                // button is pressed, toggle variable and view
                if (buttonView.isPressed) {
                    toggleTracking(locationButton)
                }
            })
        }
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "MAP READY")
        map = googleMap
        try { //add raw kml data to overlay train lines on map
            val lines = KmlLayer(map, R.raw.cta_rail_layer, requireContext())
            stops = KmlLayer(map, R.raw.cta_stops, requireContext())
            lines.addLayerToMap()
            markerInfoWindowAdapter = MarkerInfoWindowAdapter(context)
            stops = KmlLayer(map, R.raw.cta_stops, requireContext())
            // Get the button view
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
        //move camera
        val CHIAGO_ZOOM_LEVEL = 11.1f
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(CHICAGO_LATLNG, CHIAGO_ZOOM_LEVEL))
        // Prompt the user for permission.
        locationPermission
        // Turn on the My Location layer and the related control on the map.
        showUserLocation()
        // Get the current location of the device and set the position of the map.
    }

    @SuppressLint("MissingPermission")
    private fun toggleTracking(locationButton: ImageButton?) {
        Log.d(TAG, isTrackingLocation.toString())
        try {
            if (map == null) {
                Log.e(TAG, "Null map")
                return
            }
            if (!isTrackingLocation && locationPermissionGranted) {
                locationButton!!.setImageResource(R.drawable.ic_location_on)
                isTrackingLocation = true
                //                map.setMyLocationEnabled(true); //can remove updateUI method and move method sig to here
                updateUserLocationOnMap()
            } else {
                locationButton!!.setImageResource(R.drawable.ic_location_off)
                isTrackingLocation = false
                //                map.setMyLocationEnabled(false);
                locationManager!!.removeUpdates(locationListener)
            }
        } catch (e: Error) {
            Log.e(TAG + " Exception: %s", e.message!!)
        }
    }

    private fun initValueEventListener(markers: ArrayList<Marker?>, resId: Int): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trainData = ArrayList<Train>()
                try {
                    //remove previous markers
                    removeTrainMarkers(markers)
                    //populate array with train data
                    for (ds in snapshot.children) {
                        val train = ds.getValue(Train::class.java)
                        trainData.add(train as Train)
                        if (train != null) {
                            Log.d(TAG, train.toString())
                        }
                    }
                    addTrainMarkers(trainData,
                            resId,
                            markers)
                } catch (e: DatabaseException) {
                    Log.d(TAG + "DATABASE ERROR", e.message!!)
                } catch (e: NullPointerException) {
                    Log.d(TAG + "NULLPOINTER", e.message!!)
                    e.printStackTrace()
                } finally { //clear trainData to hopefully send it to GC
                    trainData.clear()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, error.message)
            }
        }
    }

    private fun updateTrainDataToScreen(trainLine: String, eventListener: ValueEventListener?) {
        dbRef.child(trainLine).addValueEventListener(eventListener!!)
    }

    private fun addTrainMarkers(trainLine: ArrayList<Train>, trainColorIconResId: Int,
                                trainMarkers: ArrayList<Marker?>) {
        try {
            for (train in trainLine) {
                val isDelay: String? = Train.formatDelay(train.isDly)
                val isApp: String? = Train.formatApp(train.isApp)
                val arrivalTime: String? = Train.formatTime(train.arrT)
                val snippet = """
                    Train Line: ${train?.getLine()}
                    Next Stop: ${train?.getNextStaNm()}
                    Dest: ${train?.getDestNm()}
                    Delays: $isDelay
                    Arrival Time: $arrivalTime
                    $isApp
                    """.trimIndent()
                val bitmap = CreateBitmap.createBitmap(context, trainColorIconResId)
                val m1 = MarkerOptions()
                        .position(train!!.generateLatLng())
                        .rotation(train.heading.toFloat())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap!!))
                        .title("Train Info")
                        .snippet(snippet)
                val onMap = map!!.addMarker(m1)
                map!!.setInfoWindowAdapter(markerInfoWindowAdapter)
                trainMarkers.add(onMap)
            }
        } catch (e: NullPointerException) {
            Log.d(TAG, e.localizedMessage)
        }
    }

    fun toggleStops() {
        try {
            if (stops != null) {
                if (!isAddedToMap) {
                    Log.d(TAG, "stops added")
                    stops!!.addLayerToMap()
                    isAddedToMap = true
                } else {
                    stops!!.removeLayerFromMap()
                    isAddedToMap = false
                    Log.d(TAG, "stops removed")
                }
            }
        } catch (e: NullPointerException) {
        } catch (e: Error) {
            Log.d(TAG, e.message + "Toggle Error")
        }
    }

    override fun onPause() {
        //need to remove listeners to free up resources
        Log.d(TAG, "ON PAUSE")
        super.onPause()
    }

    companion object {
        private const val TAG = "MAP_FRAG"
        private var stops: KmlLayer? = null
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private fun removeTrainMarkers(trainMarker: ArrayList<Marker?>?) {
            try {
                for (trainM in trainMarker!!) {
                    trainM!!.remove()
                }
                trainMarker.clear()
            } catch (e: Error) {
                Log.d(TAG, e.message!!)
            } catch (e: NullPointerException) {
                Log.d(TAG, e.message!!)
            }
        }
    }
}