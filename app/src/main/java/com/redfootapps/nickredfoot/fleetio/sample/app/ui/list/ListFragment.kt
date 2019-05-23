package com.redfootapps.nickredfoot.fleetio.sample.app.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import com.redfootapps.nickredfoot.fleetio.sample.app.services.FleetioApiService
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.tab.PageViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A placeholder fragment containing a simple view.
 */
class ListFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    private var listAdapter: ListAdapter? = null
    private var fuelEntriesArrayList: ArrayList<FuelEntry>? = null
    private var compositeDisposable: CompositeDisposable? = null

    companion object {
        @JvmStatic
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
//            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
//        }
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecylerView()
        loadFuelEntries()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    // Setup

    private fun setupRecylerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
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

    fun handleResponse(fuelEntries: List<FuelEntry>) {
        fuelEntriesArrayList = ArrayList(fuelEntries)
        fuelEntriesArrayList?.let {
            listAdapter = ListAdapter(it)
            recyclerView.adapter = listAdapter
        }
    }

    fun handleError(throwable: Throwable) {
        val error = true
    }
}