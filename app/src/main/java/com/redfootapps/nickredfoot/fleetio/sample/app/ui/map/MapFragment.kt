package com.redfootapps.nickredfoot.fleetio.sample.app.ui.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import com.redfootapps.nickredfoot.fleetio.sample.app.models.Geolocation
import com.redfootapps.nickredfoot.fleetio.sample.app.services.FleetioApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_map.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.redfootapps.nickredfoot.fleetio.sample.app.R


class MapFragment: Fragment() {

    private var compositeDisposable: CompositeDisposable? = null
    private var markerHashMap: HashMap<Marker, FuelEntry> = HashMap()

    var fuelEntriesArrayList: ArrayList<FuelEntry>? = null

    companion object  {
        @JvmStatic
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)
        loadFuelEntries()
    }

    override fun onResume() {
        super.onResume()

        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()

        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        mapView.onLowMemory()
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
            .subscribe(this::addMarkers))
    }


    fun handleError(throwable: Throwable) {
        val error = true
    }

    fun addMarkers(fuelEntries: List<FuelEntry>) {
        fuelEntriesArrayList = ArrayList(fuelEntries)

        mapView.getMapAsync { googleMap ->
            googleMap.clear()

            val boundsBuilder = LatLngBounds.Builder()

            for (fuelEntry in fuelEntries) {
                fuelEntry.geolocation?.let { geolocation ->
                    if (geolocation.latitude != null && geolocation.longitude != null) {
                        val latLng = LatLng(geolocation.latitude, geolocation.longitude)

                        val markerOptions = MarkerOptions()
                        markerOptions.position(latLng)
                        markerOptions.title(fuelEntry.reference)
                        val marker = googleMap.addMarker(markerOptions)

                        markerHashMap[marker] = fuelEntry

                        boundsBuilder.include(latLng)
                    }
                }
            }

            googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    if (markerHashMap.contains(marker)) {
                        val fuelEntry = markerHashMap.get(marker)
                        // Navigate to details
                    }
                    return false
                }
            })


            val bounds = boundsBuilder.build()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
        }
    }

}
