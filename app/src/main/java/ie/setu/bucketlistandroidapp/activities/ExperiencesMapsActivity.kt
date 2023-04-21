package ie.setu.bucketlistandroidapp.activities

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import ie.setu.bucketlistandroidapp.R
import ie.setu.bucketlistandroidapp.databinding.ActivityExperiencesMapsBinding
import ie.setu.bucketlistandroidapp.databinding.ContentExperiencesMapsBinding
import ie.setu.bucketlistandroidapp.main.MainApp
import java.util.*

class ExperiencesMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityExperiencesMapsBinding
    private lateinit var contentBinding: ContentExperiencesMapsBinding
    private lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp

        binding = ActivityExperiencesMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentBinding = ContentExperiencesMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }

    }

    @Suppress("NAME_SHADOWING")
    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.experiences.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it.id
            // For receiving information for CardView
            map.setOnMarkerClickListener(this)
            // Initial camera position view
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 1f))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val experience = app.experiences.findById(tag)
        contentBinding.currentTitle.text = experience!!.title

        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        if (experience.dueDate == Date(0)) {
            contentBinding.currentDueDate.text = getString(R.string.no_date_yet)
        } else {
            contentBinding.currentDueDate.text = formatter.format(experience.dueDate)
        }


        val isAchieved = experience.achieved
        // If experience achieved is TRUE: We display "Achieved" plus a Checkmark that comes from our drawable package.
        if (isAchieved) {
            contentBinding.currentAchieved.text = buildString { append("Achieved ") }
            // Here we are getting the checkmark drawable
            val checkMarkDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.baseline_check_mark)
            /* Making the drawable be on the right side of the text
            References: https://developer.android.com/reference/android/widget/TextView#setCompoundDrawablesWithIntrinsicBounds(android.graphics.drawable.Drawable,%20android.graphics.drawable.Drawable,%20android.graphics.drawable.Drawable,%20android.graphics.drawable.Drawable) */
            contentBinding.currentAchieved.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                checkMarkDrawable,
                null
            )
        } else {
            contentBinding.currentAchieved.text = buildString { append("Not yet achieved ") }
            // If the item is updated from isAchieved='true' to 'false', then we do not show the checkmark drawable
            contentBinding.currentAchieved.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                null,
                null
            )
        }

        Picasso.get().load(experience.image).into(contentBinding.currentImage)
        return false
    }


    // Lifecycle events on to the map to render map correctly
    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}