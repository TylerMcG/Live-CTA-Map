package com.McGregor.chicagotraintracker.UI

import java.util.ArrayList
import android.util.Log
import android.os.Bundle
import com.McGregor.chicagotraintracker.R
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.Arrays
import androidx.recyclerview.widget.RecyclerView
import Utils.RecyclerViewAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import androidx.fragment.app.Fragment
import java.lang.Error


class TrainsFragment : Fragment() {
    private val trainNames = ArrayList(listOf("Red Line", "Blue Line", "Brown Line",
            "Green Line", "Orange Line", "Pink Line", "Purple Line", "Yellow Line"))
    private val trainIconResID = ArrayList(listOf(R.drawable.ic_redline, R.drawable.ic_blueline,
            R.drawable.ic_brownline, R.drawable.ic_greenline, R.drawable.ic_orangeline,
            R.drawable.ic_pinkline, R.drawable.ic_purpleline, R.drawable.ic_yellowline))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_train, container, false)
        initRecylerView(view)
        return view
    }

    private fun initRecylerView(view: View) {
        try {
            val recyclerView: RecyclerView = view.findViewById(R.id.recyler_view)
            val adapter = RecyclerViewAdapter(this, trainNames, trainIconResID)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        } catch (e: Error) {
            Log.d("TAG", e.message!!)
        }
    }
}