package com.neeleshbuilds.healthsnap.ui.upload

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import android.util.Log
import androidx.core.graphics.scale

@HiltViewModel
class UploadViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val prefs: SharedPreferences = context.getSharedPreferences("SkinCarePrefs", Context.MODE_PRIVATE)

    private val labels: List<String> by lazy {
        try {
            context.assets.open("labels.txt").bufferedReader().use { reader: BufferedReader ->
                reader.readLines().also {
                    Log.d("UploadViewModel", "Loaded ${it.size} labels: $it")
                }
            }
        } catch (e: IOException) {
            Log.e("UploadViewModel", "Error loading labels: ${e.message}")
            emptyList()
        }
    }

    private val interpreter: Interpreter by lazy {
        try {
            Interpreter(loadModelFile()).also {
                Log.d("UploadViewModel", "Interpreter initialized")
            }
        } catch (e: Exception) {
            Log.e("UploadViewModel", "Error initializing interpreter: ${e.message}")
            throw RuntimeException("Failed to initialize TensorFlow Lite interpreter", e)
        }
    }

    fun analyzeImage(bitmap: Bitmap): Triple<String, String, String> {
        try {
            // Preprocess image
            val resizedBitmap = bitmap.scale(250, 250)
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 250, 250, 3), org.tensorflow.lite.DataType.FLOAT32)
            val tensorImage = TensorImage(org.tensorflow.lite.DataType.FLOAT32)
            tensorImage.load(resizedBitmap)
            inputFeature0.loadBuffer(convertBitmapToByteBuffer(tensorImage.bitmap))

            // Run inference
            val outputArray = Array(1) { FloatArray(labels.size) }
            interpreter.run(inputFeature0.buffer, outputArray)
            Log.d("UploadViewModel", "Inference output: ${outputArray[0].joinToString()}")

            // Post-process output
            val probabilities = outputArray[0]
            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
            val disease = labels.getOrElse(maxIndex) { "Unknown" }
            val suspicionRate = "${(probabilities[maxIndex] * 100).toInt()}%"
            val symptoms = when (disease) {
                "Melanoma" -> "Itching, redness"
                "Eczema" -> "Dryness, scaling"
                else -> "Consult a dermatologist"
            }

            return Triple(disease, suspicionRate, symptoms)
        } catch (e: Exception) {
            Log.e("UploadViewModel", "Error analyzing image: ${e.message}")
            throw RuntimeException("Image analysis failed: ${e.message}", e)
        }
    }

    fun saveRecentAnalysis(disease: String, suspicionRate: String, symptoms: String) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.format(Date())
        prefs.edit()
            .putString("recent_disease", disease)
            .putString("recent_suspicion_rate", suspicionRate)
            .putString("recent_symptoms", symptoms)
            .putString("recent_date", date)
            .apply()
        Log.d("UploadViewModel", "Saved recent analysis: $disease, $date")
    }

    fun getRecentAnalysis(): Triple<String, String, String>? {
        val disease = prefs.getString("recent_disease", null)
        val suspicionRate = prefs.getString("recent_suspicion_rate", null)
        val symptoms = prefs.getString("recent_symptoms", null)
        return if (disease != null && suspicionRate != null && symptoms != null) {
            Triple(disease, suspicionRate, symptoms)
        } else {
            null
        }
    }

    fun getLastAnalysisDate(): String {
        return prefs.getString("recent_date", "None") ?: "None"
    }

    private fun loadModelFile(): ByteBuffer {
        try {
            val assetFileDescriptor = context.assets.openFd("model.tflite")
            val fileInputStream = assetFileDescriptor.createInputStream()
            val fileChannel = fileInputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength).also {
                assetFileDescriptor.close()
                fileInputStream.close()
            }
        } catch (e: IOException) {
            Log.e("UploadViewModel", "Error loading model: ${e.message}")
            throw RuntimeException("Failed to load model.tflite", e)
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val imgData = ByteBuffer.allocateDirect(4 * 250 * 250 * 3)
        imgData.order(ByteOrder.nativeOrder())
        val intValues = IntArray(250 * 250)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until 250) {
            for (j in 0 until 250) {
                val value = intValues[pixel++]
                imgData.putFloat(((value shr 16) and 0xFF) / 255f) // R
                imgData.putFloat(((value shr 8) and 0xFF) / 255f)  // G
                imgData.putFloat((value and 0xFF) / 255f)         // B
            }
        }
        return imgData
    }

    override fun onCleared() {
        try {
            interpreter.close()
            Log.d("UploadViewModel", "Interpreter closed")
        } catch (e: Exception) {
            Log.e("UploadViewModel", "Error closing interpreter: ${e.message}")
        }
        super.onCleared()
    }
}