package com.redfootapps.nickredfoot.fleetio.sample.app.services

import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers


interface FleetioApiService {

    @Headers(
        "Authorization: Bearer 0bd86b93e8b27ea5408e4b4590efadecce9ccf75533dc7e2cde6e328e394668a",
        "Account-Token: 798819214b"
    )
    @GET("fuel_entries")
    fun getFuelEntries() : Observable<List<FuelEntry>>

}