package com.redfootapps.nickredfoot.fleetio.sample.app.ui.details

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailsAdapter (private val detailModels: ArrayList<DetailsModel>) : RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    // Model

    data class DetailsModel(
        var title: String,
        var value: String
    )

    // Adapter

    override fun getItemCount(): Int {
        return 9
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(detailModels[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return ViewHolder(view)
    }

    // ViewHolder

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(detailsModel: DetailsModel) {
            itemView.titleTextView.text = detailsModel.title
            itemView.valueTextView.text = detailsModel.value
        }
    }
}