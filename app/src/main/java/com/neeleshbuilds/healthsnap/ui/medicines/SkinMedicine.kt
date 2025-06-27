package com.neeleshbuilds.healthsnap.ui.medicines

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


data class SkinMedicine(
    @StringRes val nameResId: Int,
    @StringRes val detailsResId: Int,
    val category: String, // e.g., "Topical", "Oral", "Natural", "Procedure"
    @DrawableRes val iconResId: Int // Icon for the treatment

)