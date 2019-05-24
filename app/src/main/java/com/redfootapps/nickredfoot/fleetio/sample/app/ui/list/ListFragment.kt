package com.redfootapps.nickredfoot.fleetio.sample.app.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import kotlinx.android.synthetic.main.fragment_list.*
import android.support.v7.widget.DividerItemDecoration
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.BaseFragment
import com.redfootapps.nickredfoot.fleetio.sample.app.viewmodel.FuelEntryViewModel

interface OnFuelEntryListRefreshListener {
    fun refresh()
}

class ListFragment : BaseFragment(), ListAdapterListener {

    // Instance Variables

    private var callback: OnFuelEntryListRefreshListener? = null
    private var listAdapter: ListAdapter? = null

    // Constructor

    companion object {
        @JvmStatic
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    // Lifecycle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecylerView()
    }

    // Setup

    private fun setupRecylerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
        )
        swipeRefreshLayout.setOnRefreshListener {
            callback?.refresh()
        }
    }

    // OnNetworkFinishedListener

    override fun observeFuelEntries(viewModel: FuelEntryViewModel) {
        viewModel.fuelEntriesArrayList.observe(this, android.arch.lifecycle.Observer {
            it?.let {
                listAdapter = ListAdapter(it, this)
                recyclerView.adapter = listAdapter

                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    // Listener

    fun setOnFuelEntryListRefreshListener(callback: OnFuelEntryListRefreshListener) {
        this.callback = callback
    }

    // ListAdapterListener

    override fun itemSelected(fuelEntry: FuelEntry) {
        navigateToDetails(fuelEntry)
    }
}