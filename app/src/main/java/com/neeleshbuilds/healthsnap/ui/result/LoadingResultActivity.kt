package com.neeleshbuilds.healthsnap.ui.result

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.databinding.ActivityLoadingResultBinding
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingResultBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityLoadingResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate logo
        binding.waitImg.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.rotate)
        )

        // Animate card entrance
        binding.waitRoot.alpha = 0f
        binding.waitRoot.translationY = 100f
        binding.waitRoot.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .start()

        // Animate progress steps
        val steps = listOf(
            binding.waitRoot.findViewById<View>(R.id.progress_step_1),
            binding.waitRoot.findViewById<View>(R.id.progress_step_2),
            binding.waitRoot.findViewById<View>(R.id.progress_step_3)
        )
        steps.forEachIndexed { index, step ->
            step?.let {
                it.alpha = 0f
                it.translationX = 50f
                it.animate()
                    .alpha(1f)
                    .translationX(0f)
                    .setDuration(300)
                    .setStartDelay(200 + index * 200L)
                    .start()
            }
        }

        // Navigate to ResultActivity after delay
        handler.postDelayed({
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("disease", intent.getStringExtra("disease"))
                putExtra("suspicionRate", intent.getStringExtra("suspicionRate"))
                putExtra("symptoms", intent.getStringExtra("symptoms"))
            }
            startActivity(intent)
            finish()
        }, 2500)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}