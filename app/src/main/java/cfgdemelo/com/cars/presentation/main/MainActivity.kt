package cfgdemelo.com.cars.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import cfgdemelo.com.cars.R
import cfgdemelo.com.cars.presentation.main.favorites.FavoriteFragment
import cfgdemelo.com.cars.presentation.main.map.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private val mapFragment: MapFragment = MapFragment.newInstance()
    private val favoriteFragment: FavoriteFragment = FavoriteFragment.newInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupScreenBehavior()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_map -> {
                switchFragment(mapFragment, MAP_TAG)
            }
            R.id.navigation_favorites -> {
                switchFragment(favoriteFragment, FAVORITES_TAG)
            }
        }
        return true
    }

    private fun setupScreenBehavior() {
        main_BottomNavigation.setOnNavigationItemSelectedListener(this)
        switchFragment(mapFragment, MAP_TAG)
    }

    private fun switchFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flFrame, fragment, tag)
            .commit()
    }

    companion object {
        private const val MAP_TAG = "mapsTag"
        private const val FAVORITES_TAG = "favoritesTag"
    }
}