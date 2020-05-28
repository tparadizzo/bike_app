package com.paradizzo.bikeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

/*
* Tabita Lopes Paradizzo
* StudentID: 19551
* Email: tabitalp@hotmail.com
*/

class ListStationActivity : AppCompatActivity() {

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
        // Connects it to the layout in activity_list_station
        setContentView(R.layout.activity_list_station)

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

        getBikeStationJsonData()
    }

    /*
    * Gets the list of stations from the JC Deaux
     */
    private fun getBikeStationJsonData() {
        val url = getString(R.string.DUBLIN_BIKE_API_URL) + getString(R.string.DUBLIN_BIKE_API_KEY)
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()

                updateList(gson.fromJson(body, Array<BikeStation>::class.java).toList())
            }
        })
    }

    private fun updateList(stationList: List<BikeStation>) {
        runOnUiThread {
            viewAdapter.update(stationList)
        }
    }

}