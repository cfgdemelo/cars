package cfgdemelo.com.cars.presentation.main.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cfgdemelo.com.cars.R
import cfgdemelo.com.cars.presentation.main.MainViewModel
import cfgdemelo.com.cars.presentation.extensions.toastLong
import kotlinx.android.synthetic.main.favorite_item_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoriteFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObservers()
        viewModel.getCarList(false)
    }

    private fun setupViewModelObservers() {
        viewModel.cars.observe(viewLifecycleOwner, Observer {
            list.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = MyFavoriteRecyclerViewAdapter(it)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            context?.toastLong(it)
        })
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}