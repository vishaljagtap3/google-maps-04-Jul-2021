package `in`.bitcode.googlemaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import `in`.bitcode.googlemaps.databinding.ActivityMapsBinding
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.model.*
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    lateinit var bitMarker: Marker
    lateinit var mumMarker: Marker
    var markers = ArrayList<Marker>()
    lateinit var circle : Circle

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

        map.setOnMapClickListener(MyOnMapClickListener())
        map.setOnMarkerClickListener(MyOnMarkerClickListener())
        map.setOnMarkerDragListener(MyOnMarkerDragListener())
        map.setOnInfoWindowClickListener(MyOnInfoWindowClickListener())

        map.setInfoWindowAdapter(MyInfoWindowAdapter())

        map.setOnCircleClickListener(MyOnCircleClickListener())
        addShapes()

        misc()
    }

    private fun misc() {

        mt("Location --> 11.178402,24.049911")
        var screenLocation : Point = map.projection.toScreenLocation( LatLng(11.178402,24.049911))
        mt("Point is ${screenLocation.x} ${screenLocation.y}")

        var latLng = map.projection.fromScreenLocation(screenLocation)
        mt("Projection returned lat lng ${latLng.latitude} ${latLng.longitude}")

        var gc = Geocoder(this, Locale.getDefault())
        //var addresses = gc.getFromLocation(bitMarker.position.latitude, bitMarker.position.longitude, 50)
        var addresses = gc.getFromLocationName("Bitcode", 10)
        for(address  in addresses) {
            mt("${address.getAddressLine(0)}")
            mt("${address.getAddressLine(1)}")
            mt("Postal code: ${address.postalCode}")
            mt("${address.countryName}")
            mt("Phone: ${address.phone}")
            mt("-------------------------------------------------")
        }


    }


    private inner class MyOnCircleClickListener : GoogleMap.OnCircleClickListener {
        override fun onCircleClick(circle: Circle) {
            mt("Circle clicked!");
        }
    }

    private fun addShapes() {

        circle = map.addCircle(
            CircleOptions()
                .center(bitMarker.position)
                .clickable(true)
                .radius(5000.0)
                .fillColor(
                    Color.argb(90, 255, 0, 0)
                )
                .strokeColor(Color.RED)
        )

        var polygon = map.addPolygon(
            PolygonOptions()
                .add(mumMarker.position)
                .add(bitMarker.position)
                .add(LatLng(21.1458, 79.0882))
                .add(LatLng(22.7196, 75.8577))
                .add(LatLng(26.9124, 75.7873))
                .fillColor(Color.argb(90, 0, 0, 255))
                .strokeColor(Color.BLUE)
        )


    }


    private inner class MyInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        override fun getInfoWindow(marker: Marker): View? {
            return null
        }

        override fun getInfoContents(marker: Marker): View? {
            var view = layoutInflater.inflate(R.layout.info_window, null)
            view.findViewById<ImageView>(R.id.imgInfoWindow).setImageResource(
                R.mipmap.ic_launcher
            )
            view.findViewById<TextView>(R.id.txtInfoWindow).setText(
                marker.title
            )
            return view
        }
    }


    private inner class MyOnInfoWindowClickListener : GoogleMap.OnInfoWindowClickListener {
        override fun onInfoWindowClick(marker: Marker) {
            mt("Info Window Clicked: ${marker.title}")
        }
    }


    private inner class MyOnMarkerDragListener : GoogleMap.OnMarkerDragListener {
        override fun onMarkerDragStart(marker: Marker) {
            mt("Drag Start: ${marker.title} ${marker.position.latitude} ${marker.position.longitude}")
        }

        override fun onMarkerDrag(marker: Marker) {
            mt("Drag: ${marker.title} ${marker.position.latitude} ${marker.position.longitude}")
        }

        override fun onMarkerDragEnd(marker: Marker) {
            mt("Drag End: ${marker.title} ${marker.position.latitude} ${marker.position.longitude}")
        }
    }

    private inner class MyOnMarkerClickListener : GoogleMap.OnMarkerClickListener {
        override fun onMarkerClick(marker: Marker): Boolean {
            mt("Marker Clicked : ${marker.title}");
            return false;
        }
    }

    private inner class MyOnMapClickListener : GoogleMap.OnMapClickListener {
        override fun onMapClick(position: LatLng) {

            markers.add(
                map.addMarker(
                    MarkerOptions()
                        .title("Some Marker!")
                        .position(position)
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_YELLOW
                            )
                        )
                )
            )
            //move camera
            /*map.moveCamera(
                CameraUpdateFactory.newLatLng(bitMarker.position)
            )*/
            //map.animateCamera(CameraUpdateFactory.newLatLng(bitMarker.position))
            //map.animateCamera(CameraUpdateFactory.newLatLngZoom(bitMarker.position, 18F))

            var cameraPosition = CameraPosition.builder()
                .target(bitMarker.position)
                .tilt(80F)
                .bearing(90f)
                .zoom(18F)
                .build()

            var cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            map.animateCamera(
                cameraUpdate,
                5000,
                null
            )
        }
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
               /* .icon(
                    //BitmapDescriptorFactory.fromBitmap(flag)
                    BitmapDescriptorFactory.fromResource(
                        R.mipmap.ic_launcher
                    )

                )*/
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

    private fun mt(text : String) {
        Log.e("tag", text)
    }
}