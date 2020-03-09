package cfgdemelo.com.cars.presentation.main.map

import android.Manifest
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.ArrayList
import kotlin.math.min

class MapFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: MainViewModel by sharedViewModel()
    private lateinit var map: GoogleMap
    private val markers = arrayListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return
                }

            }
            it.isMyLocationEnabled = true
        }
    }

    private fun setupViewModelObservers() {
        viewModel.cars.observe(viewLifecycleOwner, Observer {
            it.map { car -> setMapData(LatLng(car.latitude, car.longitude), car.model) }
            setMapZoom(markers)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            context?.toastLong(it)
        })
    }

    private fun setMapData(latLng: LatLng, title: String) {
        markers.add(
            map.addMarker(
                MarkerOptions().position(latLng).title(
                    title
                )
            )
        )
    }

    private fun setMapZoom(markers: ArrayList<Marker>) {
        val builder = LatLngBounds.builder()
        for (marker in markers) {
            builder.include(LatLng(marker.position.latitude, marker.position.longitude))
        }
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), getMapPadding()))
    }

    private fun getMapPadding(): Int {
        activity?.resources?.displayMetrics?.let {
            it.widthPixels.let { width ->
                it.heightPixels.apply {
                    return min(width, this).div(2.5).toInt()
                }
            }
        }
        return MAP_ZOOM
    }

    companion object {
        private const val MAP_ZOOM = 75

        fun newInstance() = MapFragment()
    }
}