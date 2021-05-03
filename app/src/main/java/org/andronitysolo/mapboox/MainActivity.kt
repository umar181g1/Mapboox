package org.andronitysolo.mapboox

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PermissionsListener {
    private var permissionsManager: PermissionsManager?=null
    private var mapboxMap: MapboxMap?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "pk.eyJ1IjoidW1hcjE4MWcxIiwiYSI6ImNraHNyOGwzaTBnZjAyeHBpZWhuMzVibzYifQ.CHRqFKA1HjRqXotC4awSUw")

        setContentView(R.layout.activity_main)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            this.mapboxMap=it

            it.setStyle(Style.MAPBOX_STREETS, Style.OnStyleLoaded { style ->
                enableLocationComponent(style)

                val iconFactory=IconFactory.getInstance(this)
                val icon=iconFactory.fromResource(R.drawable.marker)

                it?.addMarker(MarkerOptions()
                        .position(LatLng(-7.5603894, 110.771847))
                        .title("Umar Home")
                        .icon(icon))


                val position=CameraPosition.Builder()
                        .target(LatLng(-7.5603894, 110.771847))
                        .zoom(12.0)
                        .tilt(20.0)
                        .build()

                it.animateCamera(CameraUpdateFactory.newCameraPosition(position))
            })

        }




        fun onStart() {
            super.onStart()
            mapView.onStart()
        }

        fun onResume() {
            super.onResume()
            mapView.onResume()
        }

        fun onPause() {
            super.onPause()
            mapView.onPause()
        }

        fun onStop() {
            super.onStop()
            mapView.onStop()
        }


        fun onLowMemory() {
            super.onLowMemory()
            mapView.onLowMemory()
        }


        fun onDestroy() {
            super.onDestroy()
            mapView.onDestroy()
        }


    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap?.getStyle(Style.OnStyleLoaded {
                enableLocationComponent(it)
            })
        } else {
            finish()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun enableLocationComponent(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponent=mapboxMap?.locationComponent

            locationComponent?.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build())

            if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationComponent?.isLocationComponentEnabled=true

            locationComponent?.cameraMode=CameraMode.TRACKING

            locationComponent?.renderMode=RenderMode.COMPASS
        } else {
            permissionsManager=PermissionsManager(this)
            permissionsManager?.requestLocationPermissions(this)
        }
    }
}

