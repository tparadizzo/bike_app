package com.paradizzo.bikeapp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

/*
* Tabita Lopes Paradizzo
* StudentID: 19551
* Email: tabitalp@hotmail.com
*/

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var listOfBikeStations: List<BikeStation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Connects it to the layout in activity_maps
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /*
    * Create menu icon in the map page
    */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /*
    * Show the option's page when selected
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        // Connects it to the layout in action_list, if stations list is selected
        if (id == R.id.action_list) {
            val intent = Intent(this, ListStationActivity::class.java)
            startActivity(intent)
        }

        // Connects it to the layout in favorites, if favorites option is selected
        if (id == R.id.favorites) {
            val intent = Intent(this, ListFavorites::class.java)
            startActivity(intent)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        Log.i(getString(R.string.MAPLOGGING), "onResume")
    }

    /*
     * Manipulates the map
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getBikeStationJsonData()
        setMarkerListener()
    }

    /*
    * Connects to the json-based Dublin Bikes API in JC Decaux
    */
    private fun getBikeStationJsonData() {
        Log.i(getString(R.string.MAPLOGGING), "Loading JSON data")

        val url = getString(R.string.DUBLIN_BIKE_API_URL) + getString(R.string.DUBLIN_BIKE_API_KEY)

        Log.i(getString(R.string.MAPLOGGING), url)

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                Log.i(getString(R.string.MAPLOGGING), "http fail")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i(getString(R.string.MAPLOGGING), "http success")

                val body: String = response.body?.string()!!

                Log.i(getString(R.string.MAPLOGGING), body)

                val gson = GsonBuilder().create()
                listOfBikeStations = gson.fromJson(body, Array<BikeStation>::class.java).toList()

                renderListOfBikeStationMarkers()
            }
        })
    }

    /*
    * Create the markers in the map and show its name
     */
    fun renderListOfBikeStationMarkers() {
        runOnUiThread {
            listOfBikeStations.forEach {
                val position = LatLng(it.position.lat, it.position.lng)
                val markerOne = mMap.addMarker(
                        MarkerOptions().position(position).title("Marker in ${it.address}")
                    )
                markerOne.setTag(it.number)
                Log.i(getString(R.string.MAPLOGGING), it.address)
            }

            val centreLocation = LatLng(53.349562, -6.278198)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centreLocation, 16.0f))
        }
    }

    /*
    * Makes marker work
     */
    private fun setMarkerListener() {
        mMap.setOnMarkerClickListener { marker ->
            Log.i(getString(R.string.MAPLOGGING), getString(R.string.MARKERCLICKED))

            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }

            // Show the details from the station when marker is clicked
            val intent = Intent(this, StationActivity::class.java)
            intent.putExtra("id", marker.getTag().toString())

            val chosenBikeStation = listOfBikeStations.first{it.number == marker.getTag()}
            intent.putExtra("BikeStation", chosenBikeStation)
            startActivity(intent)

            true
        }
    }
}
