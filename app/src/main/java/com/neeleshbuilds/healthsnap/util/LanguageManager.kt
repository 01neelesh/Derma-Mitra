package com.neeleshbuilds.healthsnap.util

import android.content.Context
import android.content.res.Configuration
import com.neeleshbuilds.healthsnap.ui.more.MoreAboutDiseaseActivity

import java.util.*
import androidx.core.content.edit

object LanguageManager {
    private const val PREFS_NAME = "DermaMitraPrefs"
    private const val KEY_LANGUAGE = "language"

    fun updateLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit() { putString(KEY_LANGUAGE, language) }
    }

    fun getCurrentLanguage(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString("language", "en") ?: "en"
    }
}
