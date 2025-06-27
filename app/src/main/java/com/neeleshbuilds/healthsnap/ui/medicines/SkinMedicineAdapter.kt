package com.neeleshbuilds.healthsnap.ui.medicines


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neeleshbuilds.healthsnap.databinding.ItemSkinMedicineBinding

class SkinMedicineAdapter(
    private var medicines: List<SkinMedicine>,
    private val onClick: (SkinMedicine) -> Unit = {}
) : RecyclerView.Adapter<SkinMedicineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSkinMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicine = medicines[position]
        with(holder.binding) {
            name.setText(medicine.nameResId)
            details.setText(medicine.detailsResId)
            icon.setImageResource(medicine.iconResId)
            category.text = medicine.category
            root.setOnClickListener { onClick(medicine) }
        }
    }

    override fun getItemCount(): Int = medicines.size

    fun updateMedicines(newMedicines: List<SkinMedicine>) {
        medicines = newMedicines
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemSkinMedicineBinding) : RecyclerView.ViewHolder(binding.root)
}