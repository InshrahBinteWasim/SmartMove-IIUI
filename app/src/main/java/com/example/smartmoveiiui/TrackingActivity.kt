package com.example.smartmoveiiui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmoveiiui.data.model.BusLocation
import com.example.smartmoveiiui.databinding.ActivityTrackingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityTrackingBinding
    private lateinit var mMap: GoogleMap
    private lateinit var db: FirebaseFirestore
    private var busListener: ListenerRegistration? = null
    private val busMarkers = mutableMapOf<String, Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.trackingToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Center on IIUI
        val iiui = LatLng(33.6844, 73.0479)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(iiui, 15f))
        
        startTrackingBuses()
    }

    private fun startTrackingBuses() {
        // --- CREDIT SAVING STRATEGY: Snapshot Listener for Real-time Tracking ---
        // Instead of polling every X seconds, we listen for changes.
        // Costs 1 read per update per bus.
        busListener = db.collection("bus_locations")
            .addSnapshotListener { snapshot, error ->
                if ((error != null) || (snapshot == null)) return@addSnapshotListener

                for (change in snapshot.documentChanges) {
                    val loc = change.document.toObject(BusLocation::class.java)
                    val busId = change.document.id
                    val pos = LatLng(loc.latitude, loc.longitude)

                    if (busMarkers.containsKey(busId)) {
                        busMarkers[busId]?.position = pos
                    } else {
                        mMap.addMarker(
                            MarkerOptions()
                                .position(pos)
                                .title("Bus $busId")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)),
                        )?.also {
                            busMarkers[busId] = it
                        }
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        busListener?.remove()
    }
}
