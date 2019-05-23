package com.redfootapps.nickredfoot.fleetio.sample.app.ui.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import kotlinx.android.synthetic.main.item_list.view.*
import java.lang.ref.WeakReference

interface ListAdapterListener {
    fun itemSelected(fuelEntry: FuelEntry)
}

class ListAdapter (private val fuelEntries: ArrayList<FuelEntry>, listener: ListAdapterListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var listener = WeakReference<ListAdapterListener>(listener)

    // Adapter

    override fun getItemCount(): Int {
        return fuelEntries.count()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(fuelEntries[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    // ViewHolder

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(fuelEntry: FuelEntry, listener: WeakReference<ListAdapterListener>) {
            itemView.titleTextView.text = fuelEntry.vehicleName ?: "N/A"
            itemView.subHeaderTextView.text = fuelEntry.formattedDateString()
            itemView.subHeaderTextView2.text = fuelEntry.fuelTypeName ?: "N/A"
            itemView.setOnClickListener {
                listener.get()?.itemSelected(fuelEntry)
            }
        }
    }
}