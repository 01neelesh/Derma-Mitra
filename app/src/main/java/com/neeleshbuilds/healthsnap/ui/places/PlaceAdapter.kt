package com.neeleshbuilds.healthsnap.ui.places

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.databinding.ItemPlaceBinding

class PlaceAdapter(
    private val onClick: (Place) -> Unit = {} // Click listener like AdaptersRecycle
) : ListAdapter<Place, PlaceAdapter.ViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = getItem(position)
        Log.d("PlaceAdapter", "Binding place: ${place.name}")
        with(holder.binding) {
            name.text = place.name
            address.text = place.address
            status.setText(if (place.isOpen) R.string.open_now else R.string.closed)
            root.setOnClickListener { onClick(place) }
        }
    }

    class ViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean =
            oldItem.name == newItem.name // Use name as unique ID for dummy data

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean =
            oldItem == newItem
    }
}