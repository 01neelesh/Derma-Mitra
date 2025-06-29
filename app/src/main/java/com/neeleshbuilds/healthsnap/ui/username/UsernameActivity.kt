package com.neeleshbuilds.healthsnap.ui.username


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.neeleshbuilds.healthsnap.databinding.ActivityUsernameBinding
import com.neeleshbuilds.healthsnap.ui.profile.ProfileActivity
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsernameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsernameBinding
    private val viewModel: UsernameViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private var currentHintIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cycle through hints
        cycleHints()

        // Language buttons
        binding.enButton.setOnClickListener {
            LanguageManager.updateLanguage(this, "en")
            recreate()
        }
        binding.hiButton.setOnClickListener {
            LanguageManager.updateLanguage(this, "hi")
            recreate()
        }
        binding.guButton.setOnClickListener {
            LanguageManager.updateLanguage(this, "gu")
            recreate()
        }
        binding.urButton.setOnClickListener {
            LanguageManager.updateLanguage(this, "ur")
            recreate()
        }

        // Next button
        binding.nextButton.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            if (TextUtils.isEmpty(username)) {
                binding.usernameInput.error = getString(R.string.enter_username)
                return@setOnClickListener
            }
            viewModel.saveUsername(username)
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }

    private fun cycleHints() {
        val hints = listOf(
            R.string.username_hint_en,
            R.string.username_hint_hi,
            R.string.username_hint_gu,
            R.string.username_hint_ur
        )
        binding.usernameInput.setHint(hints[currentHintIndex])
        currentHintIndex = (currentHintIndex + 1) % hints.size
        handler.postDelayed({ cycleHints() }, 3000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}