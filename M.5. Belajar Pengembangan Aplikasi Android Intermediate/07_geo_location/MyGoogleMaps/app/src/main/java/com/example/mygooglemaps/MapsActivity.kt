package com.example.mygooglemaps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.mygooglemaps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val binding: ActivityMapsBinding by lazy { ActivityMapsBinding.inflate(layoutInflater) }

    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            uiSettings.apply {
                isZoomControlsEnabled = true
                isIndoorLevelPickerEnabled = true
                isCompassEnabled = true
                isMapToolbarEnabled = true
            }
            setOnMapLongClickListener { latLng ->
                addMarker(
                    MarkerOptions().position(latLng)
                        .title("New Marker")
                        .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                        .icon(vectorToBitmap(R.drawable.ic_android, Color.parseColor("#3DDC84")))
                )

            }
            setOnPoiClickListener { poi ->
                val poiMarker = addMarker(
                    MarkerOptions().position(poi.latLng)
                        .title(poi.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                )
            }
        }

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
        map.apply {
            addMarker(
                MarkerOptions().position(dicodingSpace)
                    .title("Dicoding space")
                    .snippet("Batik Kumeli No. 50")
            )
            animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

        }

        getMyLocation()
        setMapStyle()
        addManyMarkers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // @formatter:off
            R.id.normal_type    -> { map.mapType = GoogleMap.MAP_TYPE_NORMAL;    true }
            R.id.satellite_type -> { map.mapType = GoogleMap.MAP_TYPE_SATELLITE; true }
            R.id.terrain_type   -> { map.mapType = GoogleMap.MAP_TYPE_TERRAIN;   true }
            R.id.hybrid_type    -> { map.mapType = GoogleMap.MAP_TYPE_HYBRID;    true }
            else                -> super.onOptionsItemSelected(item)
            // @formatter:on
        }
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null) ?: run {
            Log.e("BitmapHelper", "resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) getMyLocation()
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                    this.applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            ).let { success ->
                if (!success) Log.e(TAG, "Style parsing failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style: ", e)
        }
    }

    private fun addManyMarkers() {
        listOf(
            TourismPlace("Floating Market Lembang", -6.8168954, 107.6151046),
            TourismPlace("The Great Asia Africa", -6.8331128, 107.6048483),
            TourismPlace("Rabbit Town", -6.8668408, 107.608081),
            TourismPlace("Alun-Alun Kota Bandung", -6.9218518, 107.6025294),
            TourismPlace("Orchid Forest Cikole", -6.780725, 107.637409),
        ).forEach { place ->
            val latLng = LatLng(place.lat, place.long)
            val addressName = getAddressName(place.lat, place.long)
            map.addMarker(MarkerOptions().position(latLng).title(place.name).snippet(addressName))
            boundsBuilder.include(latLng)
        }

        val bounds = boundsBuilder.build()
        map.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        return try {
            val geocoder  = Geocoder(this@MapsActivity, Locale.getDefault())
            val list = geocoder.getFromLocation(lat, lon, 1)
            list?.get(0)?.getAddressLine(0).also {
                Log.d(TAG, "getAddressName: ${it.toString()}")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}

data class TourismPlace(
    val name: String,
    val lat: Double,
    val long: Double,
)