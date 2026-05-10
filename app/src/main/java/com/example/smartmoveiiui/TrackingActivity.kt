package com.example.smartmoveiiui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmoveiiui.databinding.ActivityTrackingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityTrackingBinding
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.trackingToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // IIUI Faisal Mosque / Campus coordinates (approximate)
        val iiui = LatLng(33.6844, 73.0479)
        mMap.addMarker(MarkerOptions().position(iiui).title("IIUI Main Campus"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(iiui, 15f))
        
        // Example bus location
        val busLoc = LatLng(33.6860, 73.0490)
        mMap.addMarker(MarkerOptions().position(busLoc).title("Bus Route #2"))
    }
}
