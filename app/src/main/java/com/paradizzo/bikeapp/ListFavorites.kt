package com.paradizzo.bikeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder


/*
* Tabita Lopes Paradizzo
* StudentID: 19551
* Email: tabitalp@hotmail.com
*/

class ListFavorites : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecycleViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var stationList: ArrayList<BikeStation>

    /*
    * If user click in any station on the list, it shows the details from the station
     */
    private var listener = object : RecycleViewAdapter.OnItemClickListener {
        override fun onItemClick(bikeStation: BikeStation) {
            Log.i("onItemClick", bikeStation.address)
            showBikeStationDetails(bikeStation)
        }
    }

    /*
    * Set up the station details
     */
    private fun showBikeStationDetails(bikeStation: BikeStation) {
        val intent = Intent(this, StationActivity::class.java)
        intent.putExtra("id", bikeStation.number.toString())
        intent.putExtra("BikeStation", bikeStation)
        this.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Connects it to the layout in activity_favorite_list_station
        setContentView(R.layout.activity_favorite_list_station)

        stationList = ArrayList()
        viewManager = LinearLayoutManager(this)
        viewAdapter = RecycleViewAdapter(stationList, listener)

        // Connects to the recycler_view
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {

            setHasFixedSize(true)

            // Linear layout manager
            layoutManager = viewManager

            adapter = viewAdapter
        }

        val prefs = getSharedPreferences("com.paradizzo.bikeapp.dublinbike", Context.MODE_PRIVATE)
        val markers = prefs.getStringSet("favoriteStations", setOf())?.toMutableSet()
        val gson = GsonBuilder().create()
        val newStations: ArrayList<BikeStation> = ArrayList()
        markers?.forEach{
            val bikeStation = gson.fromJson(it, BikeStation::class.java)

            // Add new station on the list
            newStations.add(bikeStation)
        }

        updateList(newStations)
    }

    private fun updateList(stationList: List<BikeStation>) {
        runOnUiThread {
            viewAdapter.update(stationList)
        }
    }
}