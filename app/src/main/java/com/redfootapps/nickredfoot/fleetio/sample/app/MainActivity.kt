package com.redfootapps.nickredfoot.fleetio.sample.app

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import com.redfootapps.nickredfoot.fleetio.sample.app.services.FleetioApiService
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.list.ListFragment
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.list.OnFuelEntryListRefreshListener
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.tab.SectionsPagerAdapter
import com.redfootapps.nickredfoot.fleetio.sample.app.viewmodel.FuelEntryViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), OnFuelEntryListRefreshListener {

    // Instance Variables

    private lateinit var fuelEntryViewModel: FuelEntryViewModel

    private var compositeDisposable: CompositeDisposable? = null

    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        fuelEntryViewModel = ViewModelProviders.of(this).get(FuelEntryViewModel::class.java)

        compositeDisposable = CompositeDisposable()
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

        if (fragment is ListFragment) {
            fragment.setOnFuelEntryListRefreshListener(this)
        }
    }

    override fun onResume() {
        super.onResume()

        loadFuelEntries()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    // Networking

    private fun loadFuelEntries() {
        val cache = buildCache()
        val networkCacheInterceptor = buildInterceptor()

        // Create OkHttpClient for network request
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(networkCacheInterceptor)
            .build()

        // Update JSON parsing to pass Fleetio's lower case underscore key values to this app's camel case variables
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        // Creating Retrofit request
        val requestInterface = Retrofit.Builder()
            .baseUrl("https://secure.fleetio.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(FleetioApiService::class.java)

        // RXJava
        compositeDisposable?.add(requestInterface.getFuelEntries()
            .doOnError(this::handleError)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse))
    }

    private fun buildCache(): Cache {
        // A simple cache to store data responses in case lose network connection
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val httpCacheDirectory = File(cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize.toLong())
    }

    private fun buildInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            val cacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.HOURS)
                .build()

            response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }

    private fun handleError(throwable: Throwable) {
        Toast.makeText(applicationContext,"Network request failed. Please try again.",Toast.LENGTH_SHORT).show()
    }

    private fun handleResponse(fuelEntries: List<FuelEntry>) {
        fuelEntryViewModel.fuelEntriesArrayList.postValue(fuelEntries)
    }

    // OnFuelEntryListRefreshListener

    override fun refresh() {
        loadFuelEntries()
    }
}