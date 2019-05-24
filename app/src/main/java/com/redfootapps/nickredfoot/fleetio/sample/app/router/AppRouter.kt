package com.redfootapps.nickredfoot.fleetio.sample.app.router

import android.support.v4.app.Fragment
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.details.DetailsDialogFragment
import java.lang.ref.WeakReference

interface AppRouterInterface {
    fun navigateToDetails(detailsDialogModel: DetailsDialogFragment.DetailsDialogModel)
}

class AppRouter(fragment: Fragment) : AppRouterInterface {

    // Instance Variables

    private var fragment = WeakReference<Fragment>(null)

    // AppRouterInterface

    override fun navigateToDetails(detailsDialogModel: DetailsDialogFragment.DetailsDialogModel) {
        val dialog = DetailsDialogFragment.newInstance(detailsDialogModel)
        val fragmentTransaction = fragment.get()?.fragmentManager?.beginTransaction()
        dialog.show(fragmentTransaction, "Details Dialog Fragment")
    }

    init {
        this.fragment = WeakReference(fragment)
    }
}