package com.neeleshbuilds.healthsnap.ui.more

import androidx.lifecycle.ViewModel
import com.neeleshbuilds.healthsnap.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreAboutDiseaseViewModel @Inject constructor() : ViewModel() {
    fun getDiseaseDetails(disease: String): String {
        return when (disease) {
            "Melanoma" -> R.string.melanoma_details.toString()
            "Vascular Lesions" -> R.string.vascular_lesions_details.toString()
            "Basal Cell Carcinoma" -> R.string.basal_cell_carcinoma_details.toString()
            "Pigmented Keratosis" -> R.string.pigmented_keratosis_details.toString()
            "Benign Keratosis" -> R.string.benign_keratosis_details.toString()
            "Eczema" -> R.string.eczema_details.toString()
            "Actinic Keratosis" -> R.string.actinic_keratosis_details.toString()
            "Dermatofibroma" -> R.string.dermatofibroma_details.toString()
            else -> R.string.unknown_disease_details.toString()
        }.let { it }
    }
}