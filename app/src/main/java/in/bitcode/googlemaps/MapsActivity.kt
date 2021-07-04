package `in`.bitcode.googlemaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import `in`.bitcode.googlemaps.databinding.ActivityMapsBinding
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    lateinit var bitMarker: Marker
    lateinit var mumMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_maps)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        setupUI();
        addMarkers();

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun addMarkers() {
        var bitcodeOptions = MarkerOptions()
        bitcodeOptions.title("BitCode Pune")
        bitcodeOptions.snippet("Best place for Android!")
            .position(LatLng(18.5091, 73.8326))
            .anchor(0.5f, 0.5f)
            .draggable(true)
            .visible(true)
            .rotation(30f)
            .zIndex(20f)
            .icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED
                )
            )

        bitMarker = map.addMarker(bitcodeOptions)
        //bitMarker.remove()


       /* var flag = BitmapFactory.decodeResource(resources, R.drawable.flag)
        flag.width = flag.width / 5
        flag.height = flag.height / 5*/

        mumMarker = map.addMarker(
            MarkerOptions()
                .title("Mumbai")
                .snippet("This is Mumbai!")
                .position(LatLng(19.0760, 72.8777))
                .icon(
                    //BitmapDescriptorFactory.fromBitmap(flag)
                    BitmapDescriptorFactory.fromResource(
                        R.mipmap.ic_launcher
                    )

                )
        )

    }

    @SuppressLint("MissingPermission")
    private fun setupUI() {

        map.isBuildingsEnabled = true
        map.isIndoorEnabled = true
        map.isMyLocationEnabled = true
        map.isTrafficEnabled = true

        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isRotateGesturesEnabled = true
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
        map.uiSettings.isTiltGesturesEnabled = true
        map.uiSettings.isIndoorLevelPickerEnabled = true

        map.addMarker(
            MarkerOptions()
                .title("Phoenix")
                .position(LatLng(18.5621, 73.9167))
                .snippet("Nice Mall in Pune...")
        )

    }
}