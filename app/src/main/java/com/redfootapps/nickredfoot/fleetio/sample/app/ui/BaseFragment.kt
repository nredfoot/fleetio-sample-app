package com.redfootapps.nickredfoot.fleetio.sample.app.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import com.redfootapps.nickredfoot.fleetio.sample.app.router.AppRouter
import com.redfootapps.nickredfoot.fleetio.sample.app.router.AppRouterInterface
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.details.DetailsDialogFragment
import com.redfootapps.nickredfoot.fleetio.sample.app.viewmodel.FuelEntryViewModel

abstract class BaseFragment : Fragment() {

    // Instance Variables

    lateinit var router: AppRouterInterface

    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router = AppRouter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            val fuelEntryViewModel = ViewModelProviders.of(it).get(FuelEntryViewModel::class.java)
            observeFuelEntries(fuelEntryViewModel)
        }
    }

    // Navigation

    fun navigateToDetails(fuelEntry: FuelEntry) {
        val detailsDialogModel = DetailsDialogFragment.DetailsDialogModel(
            fuelEntry.formattedDateString(),
            fuelEntry.vehicleName,
            if (fuelEntry.totalAmount != null) context?.getString(R.string.money_format, fuelEntry.totalAmount) else "N/A",
            if (fuelEntry.costPerMile != null) context?.getString(R.string.money_format, fuelEntry.costPerMile) else "N/A",
            if (fuelEntry.usGallons != null) context?.getString(R.string.double_format, fuelEntry.usGallons) else "N/A",
            fuelEntry.fuelTypeName,
            if (fuelEntry.pricePerVolumeUnit != null) context?.getString(R.string.money_format, fuelEntry.pricePerVolumeUnit) else "N/A",
            fuelEntry.vendorName,
            fuelEntry.reference
        )
        router.navigateToDetails(detailsDialogModel)
    }

    // Abstract

    abstract fun observeFuelEntries(viewModel: FuelEntryViewModel)

}