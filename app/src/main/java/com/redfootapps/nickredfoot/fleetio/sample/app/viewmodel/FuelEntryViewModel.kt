package com.redfootapps.nickredfoot.fleetio.sample.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry

class FuelEntryViewModel: ViewModel() {
    val fuelEntriesArrayList = MutableLiveData<List<FuelEntry>>()
}