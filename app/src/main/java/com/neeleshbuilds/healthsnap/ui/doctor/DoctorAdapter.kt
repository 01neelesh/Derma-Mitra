package com.neeleshbuilds.healthsnap.ui.doctor


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.databinding.ItemDoctorBinding
class DoctorAdapter(private var doctors: List<DoctorItem>) :
    RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = doctors[position]
        with(holder.binding) {
            name.text = doctor.name
            specialty.setText(doctor.specialtyResId)
            availability.setText(doctor.availabilityResId)
            status.setText(if (doctor.isAvailable) R.string.available_now else R.string.not_available)
            // Optional: Display rating if UI supports
            if (doctor.rating > 0) {
                status.append(" | Rating: ${doctor.rating}")
            }
        }
    }

    override fun getItemCount(): Int = doctors.size

    fun updateDoctors(newDoctors: List<DoctorItem>) {
        val diffCallback = DoctorDiffCallback(doctors, newDoctors)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        doctors = newDoctors
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(val binding: ItemDoctorBinding) : RecyclerView.ViewHolder(binding.root)

    private class DoctorDiffCallback(
        private val oldList: List<DoctorItem>,
        private val newList: List<DoctorItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].name == newList[newItemPosition].name
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}