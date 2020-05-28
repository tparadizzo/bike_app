package com.paradizzo.bikeapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
* Tabita Lopes Paradizzo
* StudentID: 19551
* Email: tabitalp@hotmail.com
*/

/*
* Gets the details from the JC Decaux
 */
@Parcelize
class BikeStation(val number: Int, val address: String, var position: Position, val bike_stands: Int,
                  val available_bike_stands: Int, val available_bikes: Int, val status: String) : Parcelable

/*
* Gets the position details from the JC Decaux
 */
@Parcelize
class Position(var lat: Double, var lng: Double) : Parcelable
