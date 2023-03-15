package ie.setu.bucketlistandroidapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.bucketlistandroidapp.R
import ie.setu.bucketlistandroidapp.databinding.ActivityMapBinding
import ie.setu.bucketlistandroidapp.models.Location

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {


    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        location = intent.extras?.getParcelable("location")!!
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Add a marker in SETU and move the camera
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Experience Location")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.setOnMarkerDragListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }


    override fun onMarkerDrag(p0: Marker) {    }

    override fun onMarkerDragEnd(marker: Marker) {
        /* For getting only 6 decimal places instead of all 12+ decimals
         Reference: https://sebhastian.com/kotlin-string-format/ */
        location.lat = String.format("%.6f", marker.position.latitude).toDouble()
        location.lng = String.format("%.6f", marker.position.longitude).toDouble()
        location.zoom = map.cameraPosition.zoom
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
    }

    override fun onMarkerDragStart(p0: Marker) {    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        @Suppress("DEPRECATION")
        super.onBackPressed()
    }
}
