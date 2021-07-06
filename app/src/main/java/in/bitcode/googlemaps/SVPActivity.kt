package `in`.bitcode.googlemaps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.StreetViewPanoramaCamera

class SVPActivity: AppCompatActivity(), OnStreetViewPanoramaReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.svp_activity)
        var svpFragment = supportFragmentManager.findFragmentById(R.id.svpFragment) as SupportStreetViewPanoramaFragment
        svpFragment.getStreetViewPanoramaAsync(this)
    }

    override fun onStreetViewPanoramaReady(svp: StreetViewPanorama) {
        svp.setPosition(LatLng(37.754130, -122.447129))
        svp.isPanningGesturesEnabled = true
        svp.isStreetNamesEnabled = true
        svp.isZoomGesturesEnabled = true
        svp.isUserNavigationEnabled = true

        svp.setOnStreetViewPanoramaClickListener {
            StreetViewPanoramaCamera.Builder()
                .zoom(svp.getPanoramaCamera().zoom + 1)
                .tilt(svp.getPanoramaCamera().tilt + 10)
                .bearing(svp.getPanoramaCamera().bearing - 60)
                .build();
        }
    }
}