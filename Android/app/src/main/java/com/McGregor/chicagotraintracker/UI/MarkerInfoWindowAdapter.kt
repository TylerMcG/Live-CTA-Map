package com.McGregor.chicagotraintracker.UI


import android.annotation.SuppressLint

import com.McGregor.chicagotraintracker.R
import android.view.LayoutInflater
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import android.widget.TextView
import android.content.Context
import android.view.View
import com.google.android.gms.maps.model.Marker


class MarkerInfoWindowAdapter @SuppressLint("InflateParams") constructor(private val context: Context?) : InfoWindowAdapter {
    @SuppressLint("InflateParams")
    private val window = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null)
    private fun renderWindowText(marker: Marker, view: View) {
        val title = marker.title
        val tvTitle = view.findViewById<TextView>(R.id.titleTextView)
        tvTitle.setTextColor(context!!.resources.getColor(R.color.black, null))
        val snippetBody = view.findViewById<TextView>(R.id.snippetTextView)
        tvTitle.text = title
        val snippet = marker.snippet
        snippetBody.text = snippet
    }

    override fun getInfoWindow(marker: Marker): View? {
        renderWindowText(marker, window)
        return window
    }

    override fun getInfoContents(marker: Marker): View? {
        renderWindowText(marker, window)
        return window
    }

}