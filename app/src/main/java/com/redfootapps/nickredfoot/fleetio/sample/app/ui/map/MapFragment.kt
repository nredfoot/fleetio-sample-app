package com.redfootapps.nickredfoot.fleetio.sample.app.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.MarkerOptions
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import kotlinx.android.synthetic.main.fragment_map.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.BaseFragment


class MapFragment: BaseFragment() {

    private var markerHashMap: HashMap<Marker, FuelEntry> = HashMap()

    companion object  {
        @JvmStatic
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    // Lifecycle


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()

        mapView.onPause()
    }

    override fun handleResponse(fuelEntries: List<FuelEntry>) {
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

            googleMap.setOnMarkerClickListener { marker ->
                if (markerHashMap.contains(marker)) {
                    markerHashMap[marker]?.let { fuelEntry ->
                        navigateToDetails(fuelEntry)
                    }
                }
                false
            }


            val bounds = boundsBuilder.build()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
        }
    }
}
