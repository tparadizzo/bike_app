package com.paradizzo.bikeapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_station.*

/*
* Tabita Lopes Paradizzo
* StudentID: 19551
* Email: tabitalp@hotmail.com
*/

class StationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var selectedBikeStation: BikeStation

    /*
    * Create station page with it's details
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Connects it to the layout in acticity_station
        setContentView(R.layout.activity_station)

        val intent = intent
        val data = intent.extras

        selectedBikeStation = data?.getParcelable<BikeStation>("BikeStation")!!

        val stationId: String = intent.getStringExtra("id")!!

        Log.i(getString(R.string.MAPLOGGING), stationId)
        Log.i(getString(R.string.MAPLOGGING), "intent called")

        // Set up the details from the station in real time, connected to JC Decaux
        textViewStationTitle.text = selectedBikeStation.address
        textViewStationId.text = "Station $stationId"
        textViewStationStatus.text = selectedBikeStation.status
        textViewStationStand.text = "Bike Stands " + selectedBikeStation.bike_stands.toString()
        textViewBikesAvailable.text = selectedBikeStation.available_bikes.toString()
        textViewStationAvailable.text = selectedBikeStation.available_bike_stands.toString()

        // Change color from the word if it's is open (gets green) if it's closed (gets red)
        if(selectedBikeStation.status == "OPEN") {
            textViewStationStatus.setTextColor(Color.parseColor("#006400"))
        } else {
            textViewStationStatus.setTextColor(Color.parseColor("#FF0000"))
        }

        // Change color from the numbers of bikes availables.
        // If there are more than 50% available it gets green, if there are less than 50% it gets red.
        if(selectedBikeStation.available_bikes >= (selectedBikeStation.bike_stands / 2)) {
            textViewBikesAvailable.setTextColor(Color.parseColor("#006400"))
        } else {
            textViewBikesAvailable.setTextColor(Color.parseColor("#FF0000"))
        }

        // Change color from the numbers of stands availables.
        // If there are more than 50% available it gets green, if there are less than 50% it gets red.
        if(selectedBikeStation.available_bike_stands >= (selectedBikeStation.bike_stands / 2)) {
            textViewStationAvailable.setTextColor(Color.parseColor("#006400"))
        } else {
            textViewStationAvailable.setTextColor(Color.parseColor("#FF0000"))
        }

        buttonSavePreferences.setOnClickListener {
            saveAsFavourite(selectedBikeStation)
        }

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    /*
    * When user click on "save preferences" button, it will save the station on favorites list
     */
    private fun saveAsFavourite(bikeStation: BikeStation) {
        val prefs = getSharedPreferences("com.paradizzo.bikeapp.dublinbike", Context.MODE_PRIVATE)
        val markers = prefs.getStringSet("favoriteStations", setOf())?.toMutableSet()
        val gson = GsonBuilder().create()
        val favorites: ArrayList<BikeStation> = ArrayList()
        markers?.forEach {
            favorites.add(gson.fromJson(it, BikeStation::class.java))
        }
        if (!favorites.contains(bikeStation)) {
            markers?.add(gson.toJson(bikeStation))

            val editor = prefs.edit()
            editor.clear()
            editor.commit()
            editor.putStringSet("favoriteStations", markers)
            editor.apply()
            editor.commit()
        }
    }

    /*
    * Set up the map on the station details page, showing just the station selected before.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val markerLatLng = LatLng(selectedBikeStation.position.lat, selectedBikeStation.position.lng)
        mMap.addMarker(MarkerOptions().position(markerLatLng).title(selectedBikeStation.address))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 16.0f))
    }

    /*
    * Add the mapView lifecycle to the activity's lifecycle methods
     */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}