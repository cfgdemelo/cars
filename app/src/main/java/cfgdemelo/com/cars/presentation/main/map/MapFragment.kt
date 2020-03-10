package cfgdemelo.com.cars.presentation.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import cfgdemelo.com.cars.R
import cfgdemelo.com.cars.presentation.main.MainViewModel
import cfgdemelo.com.cars.presentation.extensions.toastLong
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.layout_info_window.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.ArrayList
import kotlin.math.min
import kotlin.math.roundToInt

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location

    private val viewModel: MainViewModel by sharedViewModel()
    private val markers = arrayListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        setupViewModelObservers()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            map = it
            viewModel.getCarList(true)
            context?.let { context ->
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    startLocationPermissionRequest()
                    return
                } else {
                    getLastLocation()
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
                } else {
                    context?.getString(R.string.location_error)?.let {
                        context?.toastLong(it)
                    }
                }
            }
        }
    }

    private fun setupViewModelObservers() {
        viewModel.cars.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                it.map { car ->
                    markers.add(
                        map.addMarker(
                            viewModel.getMapData(
                                LatLng(car.latitude, car.longitude),
                                car.model.substring(0, 25),
                                location
                            )
                        )
                    )
                }
                setMapZoom(markers)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            context?.toastLong(it)
        })
    }

    private fun startLocationPermissionRequest() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                activity?.let { activity ->
                    map.setInfoWindowAdapter(MapInfoWindowAdapter(activity))
                }
                map.isMyLocationEnabled = true
                this.location = it
            }
        }
    }

    private fun setMapZoom(markers: ArrayList<Marker>) {
        LatLngBounds.builder().apply {
            markers.map {
                it.showInfoWindow()
                include(LatLng(it.position.latitude, it.position.longitude))
            }
            include(LatLng(location.latitude, location.longitude))
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(this.build(), getMapPadding()))
        }
    }

    private fun getMapPadding(): Int {
        activity?.resources?.displayMetrics?.let {
            it.widthPixels.let { width ->
                it.heightPixels.apply {
                    return min(width, this).div(8)
                }
            }
        }
        return MAP_ZOOM
    }

    companion object {
        private const val MAP_ZOOM = 75
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1234

        fun newInstance() = MapFragment()
    }
}

class MapInfoWindowAdapter(private val activity: Activity) : GoogleMap.InfoWindowAdapter {
    @SuppressLint("InflateParams")
    override fun getInfoContents(marker: Marker?): View {
        activity.layoutInflater.inflate(R.layout.layout_info_window, null).apply {
            this.tvCarInfo.text = marker?.title
            this.tvDistanceInfo.text = marker?.snippet
            return this
        }
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

}