package com.redfootapps.nickredfoot.fleetio.sample.app.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter (private val fuelEntries: ArrayList<FuelEntry>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    // Adapter

    override fun getItemCount(): Int {
        return fuelEntries.count()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(fuelEntries[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    // ViewHolder

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(fuelEntry: FuelEntry) {
            itemView.titleTextView.text = fuelEntry.date
            itemView.subHeaderTextView.text = fuelEntry.vehicleName
            itemView.subHeaderTextView2.text = fuelEntry.fuelTypeName
        }
    }
}