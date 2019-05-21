package com.redfootapps.nickredfoot.fleetio.sample.app.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

class MapFragment: Fragment() {

    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        compositeDisposable = CompositeDisposable()
    }



    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): MapFragment {
            return MapFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}
