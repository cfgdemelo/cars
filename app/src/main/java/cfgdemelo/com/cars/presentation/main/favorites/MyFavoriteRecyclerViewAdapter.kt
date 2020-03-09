package cfgdemelo.com.cars.presentation.main.favorites

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cfgdemelo.com.cars.R
import cfgdemelo.com.cars.data.model.Car

import kotlinx.android.synthetic.main.favorite_item.view.*

class MyFavoriteRecyclerViewAdapter(
    private val values: List<Car>
) : RecyclerView.Adapter<MyFavoriteRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.model.text = item.model
        holder.brand.text = item.brand
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val model: TextView = view.tvModel
        val brand: TextView = view.tvBrand
    }
}