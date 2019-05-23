package com.redfootapps.nickredfoot.fleetio.sample.app.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import com.redfootapps.nickredfoot.fleetio.sample.app.router.AppRouter
import com.redfootapps.nickredfoot.fleetio.sample.app.router.AppRouterInterface
import com.redfootapps.nickredfoot.fleetio.sample.app.services.FleetioApiService
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.details.DetailsDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseFragment : Fragment() {

    lateinit var router: AppRouterInterface

    var compositeDisposable: CompositeDisposable? = null
    var fuelEntriesArrayList: ArrayList<FuelEntry>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router = AppRouter(this)

        compositeDisposable = CompositeDisposable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFuelEntries()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    fun loadFuelEntries() {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val requestInterface = Retrofit.Builder()
            .baseUrl("https://secure.fleetio.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(FleetioApiService::class.java)

        compositeDisposable?.add(requestInterface.getFuelEntries()
            .doOnError(this::handleError)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse))
    }

    fun handleError(throwable: Throwable) {
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        toast.setText("Network request failed. Please try again.")
        toast.show()
    }

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

    abstract fun handleResponse(fuelEntries: List<FuelEntry>)

}