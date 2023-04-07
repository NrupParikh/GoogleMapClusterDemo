package com.nrup.gmapclusterapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.nrup.gmapclusterapp.databinding.FragmentMyGmapBinding
import com.nrup.gmapclusterapp.model.MyItem
import kotlin.math.floor


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

    @SuppressLint("PotentialBehaviorOverride")
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
            clusterManager.renderer =
                MarkerClusterRenderer((activity as MainActivity), googleMap, clusterManager)
            googleMap?.setOnCameraIdleListener(clusterManager)
            googleMap?.setOnMarkerClickListener(clusterManager)

            // disable the clustering animations
            // clusterManager.setAnimation(false)

            addItems(it)
        }
    }

    private fun addItems(googleMap: GoogleMap) {

        // Set some lat/lng coordinates to start with.
        var lat = bengaluruLatitude
        var lng = bengaluruLongitude
        var id: Int

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..9) {
            val offset = i / 60.0
            id = (i + 1)
            lat += offset
            lng += offset
            val offsetItem =
                MyItem(
                    id = id,
                    latitude = lat,
                    longitude = lng,
                    markerTitle = "Title $id",
                    markerSnippet = "Snippet $id"
                )
            clusterManager.addItem(offsetItem)
        }

        // un-cluster the markers
        clusterManager
            .setOnClusterClickListener { cluster ->
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        cluster.position, floor(
                            googleMap.cameraPosition.zoom + 1
                        ).toFloat()
                    ), 300,
                    null
                )
                true
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

class MarkerClusterRenderer(
    context: Activity?,
    map: GoogleMap?,
    clusterManager: ClusterManager<MyItem>
) :
    DefaultClusterRenderer<MyItem>(context, map, clusterManager) {
    private val iconGenerator: IconGenerator
    private val markerImageView: ImageView
    private val mContext: Activity

    init {
        iconGenerator = IconGenerator(context) // 3
        markerImageView = ImageView(context)
        markerImageView.layoutParams = ViewGroup.LayoutParams(MARKER_DIMENSION, MARKER_DIMENSION)
        iconGenerator.setContentView(markerImageView) // 4
        mContext = context!!
    }

    // Change cluster color
    override fun getColor(clusterSize: Int): Int {
        return if (clusterSize <= 4) {
            Color.GRAY
        } else if (clusterSize in 5..8) {
            Color.RED
        } else {
            ContextCompat.getColor(mContext, com.nrup.gmapclusterapp.R.color.purple_500)
        }

    }

    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) { // 5
        // markerImageView.setImageBitmap(R.drawable.location_vector_icon) // 6
        //val icon = iconGenerator.makeIcon() // 7

        val icon = bitmapDescriptorFromVector(
            item.id,
            com.nrup.gmapclusterapp.R.drawable.ic_new_marker,
            mContext
        )

        // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)) // 8
        markerOptions.icon(icon)
        markerOptions.title(item.title)
    }

    companion object {
        // 1
        private const val MARKER_DIMENSION = 48 // 2
    }

    private fun Drawable.setVectorTintColor(activity: Activity, color: Int) {
        this.setTint(ContextCompat.getColor(activity, color))
    }

    private fun bitmapDescriptorFromVector(
        id: Int,
        vectorResId: Int,
        context: Activity
    ): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(context, vectorResId)
        drawable!!.setBounds(
            0,
            0,
            drawable.intrinsicWidth,
            drawable.intrinsicHeight
        )
        if (id % 3 == 0) {
            drawable.setVectorTintColor(
                context,
                com.nrup.gmapclusterapp.R.color.yellow_color_slot
            )
        } else {
            drawable.setVectorTintColor(
                context,
                com.nrup.gmapclusterapp.R.color.green_color_slot
            )
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}