package com.nrup.gmapclusterapp.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MyItem(
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val markerTitle: String? = "",
    val markerSnippet: String? = ""
) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(lat, long)
    }

    override fun getTitle(): String? {
        return markerTitle
    }


    override fun getSnippet(): String? {
        return markerSnippet
    }

    override fun getZIndex(): Float? {
        return 0.0f
    }
}