package com.example.jukang.helper.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

data class ClassificationResult(val label: String, val confidence: Float)

class ImageClassifier(private val context: Context) {
    private lateinit var interpreter: Interpreter
    private lateinit var labels: List<String>
    private lateinit var imageProcessor: ImageProcessor

    private val modelInputWidth = 150
    private val modelInputHeight = 150

    init {
        loadModel()
        loadLabels()
        setupImageProcessor()
    }

    private fun loadModel() {
        val fileDescriptor = context.assets.openFd("model_compatible.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        val modelBuffer: MappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        val options = Interpreter.Options()
        interpreter = Interpreter(modelBuffer, options)
    }

    private fun loadLabels() {
        labels = context.assets.open("class_labels.txt").bufferedReader().readLines()
    }

    private fun setupImageProcessor() {
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(modelInputHeight, modelInputWidth, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0.0f, 255.0f))
            .build()
    }

    /**
     * FUNGSI LAMA (TETAP ADA): Mengembalikan hasil teratas sebagai String.
     * Berguna jika Anda hanya butuh jawaban cepat.
     */
    fun classify(bitmap: Bitmap): String {
        val tensorImage = TensorImage(DataType.FLOAT32).apply { load(bitmap) }
        val processedImage = imageProcessor.process(tensorImage)
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, labels.size), DataType.FLOAT32)
        interpreter.run(processedImage.buffer, outputBuffer.buffer)

        val result = outputBuffer.floatArray
        val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1

        return if (maxIndex != -1) {
            val confidence = result[maxIndex] * 100
            Log.d(TAG, "Hasil teratas: ${labels[maxIndex]} dengan confidence ${"%.2f".format(confidence)}%")
            if (confidence >= 60.0f) {
                labels[maxIndex]
            } else {
                "Tidak diketahui"
            }
        } else {
            "Unknown"
        }
    }

    fun classifyAndGetAllResults(bitmap: Bitmap): List<ClassificationResult> {
        // 1. Proses gambar seperti biasa
        val tensorImage = TensorImage(DataType.FLOAT32).apply { load(bitmap) }
        val processedImage = imageProcessor.process(tensorImage)
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, labels.size), DataType.FLOAT32)

        // 2. Jalankan inferensi
        interpreter.run(processedImage.buffer, outputBuffer.buffer)

        // 3. Dapatkan semua skor confidence
        val confidenceScores = outputBuffer.floatArray

        // 4. Ubah skor mentah menjadi List<ClassificationResult> dan urutkan
        return confidenceScores.mapIndexed { index, confidence ->
            ClassificationResult(labels[index], confidence * 100)
        }.sortedByDescending { it.confidence }
    }

    fun close() {
        interpreter.close()
    }

    companion object {
        private const val TAG = "ImageClassifier"
    }
}