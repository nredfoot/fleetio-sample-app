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


class ListFragment : BaseFragment(), ListAdapterListener {

    private var listAdapter: ListAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

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
    }

    override fun handleResponse(fuelEntries: List<FuelEntry>) {
        fuelEntriesArrayList = ArrayList(fuelEntries)
        fuelEntriesArrayList?.let {
            listAdapter = ListAdapter(it, this)
            recyclerView.adapter = listAdapter
        }
    }

    // ListAdapterListener

    override fun itemSelected(fuelEntry: FuelEntry) {
        navigateToDetails(fuelEntry)
    }
}