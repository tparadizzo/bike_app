package com.paradizzo.bikeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_one.view.*

/*
Tabita Lopes Paradizzo
StudentID: 19551
Email: tabitalp@hotmail.com
*/

/*
* Fill up all the data needed to the list
 */
class RecycleViewAdapter(
    private val ListStations: ArrayList<BikeStation>,
    private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>(){

    /*
    * When clicked show the station's details
     */
    interface OnItemClickListener {
        fun onItemClick(bikeStation: BikeStation)
    }

    fun update(newUsers: List<BikeStation>) {
        ListStations.clear()
        ListStations.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_one, parent, false)
        return ViewHolder(itemView)
    }

    /*
    * Set up the name in each station on the list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = ListStations[position]
        holder.textView1.text = currentItem.address
        holder.textView2.text = "Station: " + currentItem.number.toString()
        holder.bind(currentItem, itemClickListener)
    }

    override fun getItemCount() = ListStations.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1 : TextView = itemView.text_view_1
        val textView2 : TextView = itemView.text_view_2

        fun bind(bikeStation: BikeStation, clickListener: OnItemClickListener)
        {
            itemView.setOnClickListener {
                clickListener.onItemClick(bikeStation)
            }
        }
    }
}