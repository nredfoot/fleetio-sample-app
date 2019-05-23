package com.redfootapps.nickredfoot.fleetio.sample.app.ui.details

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.models.FuelEntry
import java.io.Serializable
import kotlinx.android.synthetic.main.fragment_details_dialog.*
import kotlinx.android.synthetic.main.fragment_list.recyclerView


class DetailsDialogFragment : DialogFragment() {

    // Models

    data class DetailsDialogModel(var date: String? = "",
                                  var vehicleName: String? = "",
                                  var cost: String? = "",
                                  var costPerMile: String? = "",
                                  var gallons: String? = "",
                                  var fuelType: String? = "",
                                  var pricePerGallon: String? = "",
                                  var vendor: String? = "",
                                  var referenceNumber: String? = "") : Serializable

    // Instance Variables

    private var detailsAdapter: DetailsAdapter? = null

    private lateinit var detailsDialogModel: DetailsDialogModel

    // Constructor

    companion object {
        @JvmStatic
        fun newInstance(detailsDialogModel: DetailsDialogModel): DetailsDialogFragment {
            val detailsFragment = DetailsDialogFragment()

            val args = Bundle()
            args.putSerializable("details_dialog_model", detailsDialogModel)
            detailsFragment.arguments = args

            return detailsFragment
        }
    }

    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(
            STYLE_NORMAL,
            R.style.AppTheme_FullScreenDialog
        )

        detailsDialogModel = arguments?.getSerializable("details_dialog_model") as DetailsDialogModel
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        // Make dialog full screen
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    // Setup

    fun setupToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp)
        toolbar.title = context?.getString(R.string.fuel_entry_details)
        toolbar.setNavigationOnClickListener{ dismiss() }
    }

    fun setupRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
        )

        detailsAdapter = DetailsAdapter(generateDetailModelsList(detailsDialogModel))
        recyclerView.adapter = detailsAdapter
    }

    // Helpers

    fun generateDetailModelsList(detailsDialogModel: DetailsDialogModel): ArrayList<DetailsAdapter.DetailsModel> {
        val dateDetailsModel = DetailsAdapter.DetailsModel("Date", detailsDialogModel.date ?: "N/A")
        val vehicleNameDetailsModel = DetailsAdapter.DetailsModel("Vehicle Name", detailsDialogModel.vehicleName ?: "N/A")
        val costNameDetailsModel = DetailsAdapter.DetailsModel("Cost", detailsDialogModel.cost ?: "N/A")
        val costPerMileNameDetailsModel = DetailsAdapter.DetailsModel("Cost Per Mile", detailsDialogModel.costPerMile ?: "N/A")
        val gallonsDetailsModel = DetailsAdapter.DetailsModel("Gallons", detailsDialogModel.gallons ?: "N/A")
        val fuelTypeDetailsModel = DetailsAdapter.DetailsModel("Fuel Type", detailsDialogModel.fuelType ?: "N/A")
        val pricePerGallonDetailsModel = DetailsAdapter.DetailsModel("Price Per Gallon", detailsDialogModel.pricePerGallon ?: "N/A")
        val vendorDetailsModel = DetailsAdapter.DetailsModel("Vendor", detailsDialogModel.vendor ?: "N/A")
        val referenceNumberDetailsModel = DetailsAdapter.DetailsModel("Reference Number", detailsDialogModel.referenceNumber ?: "N/A")

        return arrayListOf(dateDetailsModel,
            vehicleNameDetailsModel,
            costNameDetailsModel,
            costPerMileNameDetailsModel,
            gallonsDetailsModel,
            fuelTypeDetailsModel,
            pricePerGallonDetailsModel,
            vendorDetailsModel,
            referenceNumberDetailsModel)
    }
}