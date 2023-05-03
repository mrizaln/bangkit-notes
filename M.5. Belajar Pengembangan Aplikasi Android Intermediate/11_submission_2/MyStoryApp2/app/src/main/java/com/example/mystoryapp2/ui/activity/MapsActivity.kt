package com.example.mystoryapp2.ui.activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp2.R
import com.example.mystoryapp2.databinding.ActivityMapsBinding
import com.example.mystoryapp2.model.data.local.database.Story
import com.example.mystoryapp2.model.repository.RequestResult
import com.example.mystoryapp2.ui.viewmodel.MapsViewModel
import com.example.mystoryapp2.ui.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.time.Instant


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener {

    private lateinit var googleMap: GoogleMap
    private var stories: List<Story>? = null
    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    private val viewModel: MapsViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private var selectedStoryId: String? = null
    private var doubleClick = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.title = getString(R.string.activity_maps_title)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap.apply {
            uiSettings.apply {
                isZoomControlsEnabled = true
                isIndoorLevelPickerEnabled = true
                isCompassEnabled = true
                isMapToolbarEnabled = true
            }
            setOnMarkerClickListener(this@MapsActivity)
        }

        viewModel.setQuerySize(QUERY_SIZE)
        setMapStyle()

        configureStoryObserver()
    }

    // https://stackoverflow.com/a/39543436
    override fun onMarkerClick(marker: Marker): Boolean {
        val storyId = marker.tag as String
        Log.d(javaClass.simpleName, "id: $storyId")
        if (doubleClick && selectedStoryId == storyId) {
            val intent = Intent(this@MapsActivity, StoryDetailActivity::class.java)
            intent.putExtra(StoryDetailActivity.EXTRA_STORY_ID, storyId)
            startActivity(intent)
        } else {
            selectedStoryId = storyId
            doubleClick = true
            Handler().postDelayed({
                doubleClick = false
                selectedStoryId = null
            }, DOUBLE_CLICK_TIMEOUT)
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh_maps  -> viewModel.reset(QUERY_SIZE)
            R.id.action_random_marker -> highlightRandomStory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMapStyle() {
        try {
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            ).let { success ->
                if (!success) Log.e(javaClass.simpleName, "Style parsing failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(javaClass.simpleName, "Can't find style: ", e)
        }
    }

    private fun configureStoryObserver() = viewModel.stories.observe(this) { result ->
        Log.d(javaClass.simpleName, "stories observer: ${result::class.java.simpleName}")
        when (result) {
            is RequestResult.Loading -> {
                showLoading(true)
            }

            is RequestResult.Error   -> {
                Toast.makeText(
                    this@MapsActivity,
                    getString(R.string.toast_maps_load_failed, result.error),
                    Toast.LENGTH_SHORT
                ).show()
                showLoading(false)
            }

            is RequestResult.Success -> {
                showLoading(false)
                stories = result.data
                loadStories(stories!!)
            }
        }
    }

    private fun loadStories(stories: List<Story>) {
        var latestStoryTime = 0L
        var latestStory: Story? = null
        stories.forEach { story ->
            val lat = story.lat ?: run {
                Log.i(javaClass.simpleName, "loadStories: latitude is null when it should not!")
                return@forEach
            }
            val lon = story.lon ?: run {
                Log.i(
                    javaClass.simpleName, "loadStories: longitude is null when it should not!"
                )
                return@forEach
            }

            val latLng = LatLng(lat, lon)

            Log.d(
                javaClass.simpleName,
                "lat-lng: " + "%.2f".format(lat) + ", %.2f | ".format(lon) + story.name
            )

            val storyTime = Instant.parse(story.createdAt).toEpochMilli()
            if (storyTime > latestStoryTime) {
                latestStoryTime = storyTime
                latestStory = story
            }

            val markerOptions = MarkerOptions().position(latLng)
                .title(story.name)
                .snippet(DateUtils.getRelativeTimeSpanString(storyTime).toString())

            googleMap.addMarker(markerOptions)?.apply { tag = story.id }
        }

        val highlight = LatLng(latestStory!!.lat!!, latestStory!!.lon!!)
        Log.d(javaClass.simpleName, "highlight: ${highlight.latitude}, ${highlight.longitude}")

        highlightLatLng(highlight, 10f)
        Log.d(javaClass.simpleName, "Data loaded")
    }

    private fun highlightLatLng(latLng: LatLng, zoom: Float) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun highlightRandomStory() {
        stories?.random()?.let {
            val highlight = LatLng(it.lat!!, it.lon!!)
            highlightLatLng(highlight, 15f)
        }
    }

    private fun showLoading(visible: Boolean) {
        binding.pbMaps.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        private const val DOUBLE_CLICK_TIMEOUT: Long = 5000
        private const val QUERY_SIZE = 100
    }
}