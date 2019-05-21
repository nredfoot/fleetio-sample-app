package com.redfootapps.nickredfoot.fleetio.sample.app.models

data class FuelEntry(val id: Int,
                     val costPerHour: Double? = null,
                     val costPerMile: Double? = null,
                     val date: String? = null,
                     val fuelTypeId: Int? = null,
                     val fuelTypeName: String? = null,
                     val reference: String? = null,
                     val usGallons: Double? = null,
                     val usGallonsPerHour: Double? = null,
                     val totalAmount: Double? = null,
                     val vehicleName: String? = null,
                     val vendor_id: String? = null,
                     val vendor_name: String? = null,
                     val geolocation: Geolocation? = null)