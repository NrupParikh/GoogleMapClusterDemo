package com.nrup.gmapclusterapp.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MyItem(
    val id: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val markerTitle: String? = "",
    val markerSnippet: String? = "",
) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
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