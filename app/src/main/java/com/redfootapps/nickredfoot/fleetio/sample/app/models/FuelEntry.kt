package com.redfootapps.nickredfoot.fleetio.sample.app.models

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


data class FuelEntry(val id: Int,
                     val costPerMile: Double? = null,
                     val date: String? = null,
                     val fuelTypeId: Int? = null,
                     val fuelTypeName: String? = null,
                     val pricePerVolumeUnit: Double? = null, // I am using this value as price per gallon
                     val reference: String? = null,
                     val usGallons: Double? = null,
                     val usGallonsPerHour: Double? = null,
                     val totalAmount: Double? = null,           // I am using this value as cost
                     val vehicleName: String? = null,
                     val vendorId: String? = null,
                     val vendorName: String? = null,
                     val geolocation: Geolocation? = null) {

    fun formattedDateString(): String {
        return DateTimeFormat.forPattern("MMM d yyyy").print(DateTime.parse(date))
    }
}