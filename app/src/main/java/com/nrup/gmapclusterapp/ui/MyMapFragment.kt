package com.nrup.gmapclusterapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager
import com.nrup.gmapclusterapp.databinding.FragmentMyGmapBinding
import com.nrup.gmapclusterapp.model.MyItem

class MyMapFragment : Fragment(), GoogleMap.OnMarkerClickListener {
    private lateinit var binding: FragmentMyGmapBinding

    private lateinit var clusterManager: ClusterManager<MyItem>

    private var bengaluruLatitude = 12.9716
    private var bengaluruLongitude = 77.5946

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyGmapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)


        binding.mapView.getMapAsync {
            googleMap = it
            googleMap?.uiSettings?.isZoomControlsEnabled = true
            googleMap?.uiSettings?.isCompassEnabled = true

            val latLong = LatLng(bengaluruLatitude, bengaluruLongitude)

            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 10.75f))

            clusterManager = ClusterManager(requireContext(), googleMap)
            googleMap?.setOnCameraIdleListener(clusterManager)
            googleMap?.setOnMarkerClickListener(clusterManager)

            // disable the clustering animations
            // clusterManager.setAnimation(false)

            addItems()
        }
    }

    private fun addItems() {

        // Set some lat/lng coordinates to start with.
        var lat = bengaluruLatitude
        var lng = bengaluruLongitude

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..9) {
            val offset = i / 60.0
            lat += offset
            lng += offset
            val offsetItem =
                MyItem(lat, lng, "Title $i", "Snippet $i")
            clusterManager.addItem(offsetItem)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return true
    }
}