package com.example.jukang.helper.utils

import android.content.Context
import android.graphics.Bitmap
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

class ImageClassifier(private val context: Context) {
    private lateinit var interpreter: Interpreter
    private lateinit var labels: List<String>
    private lateinit var imageProcessor: ImageProcessor

    // --- DIPERBAIKI: Dimensi input disesuaikan dengan model Anda ---
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

        // Opsi ini bisa membantu jika Anda menggunakan delegasi (misal: GPU)
        val options = Interpreter.Options()
        // options.addDelegate(GpuDelegate()) // Contoh jika pakai GPU
        interpreter = Interpreter(modelBuffer, options)
    }

    private fun loadLabels() {
        labels = context.assets.open("class_labels.txt").bufferedReader().readLines()
    }

    // Menyiapkan prosesor gambar
    private fun setupImageProcessor() {
        imageProcessor = ImageProcessor.Builder()
            // Langkah 1: Ubah ukuran gambar ke dimensi yang diharapkan model
            .add(ResizeOp(modelInputHeight, modelInputWidth, ResizeOp.ResizeMethod.BILINEAR))
            // Langkah 2 (Opsional tapi umum): Normalisasi nilai piksel
            // Model Anda menggunakan rescale=1./255, jadi ini sudah benar.
            .add(NormalizeOp(0.0f, 255.0f))
            .build()
    }

    fun classify(bitmap: Bitmap): String {
        // --- DIPERBAIKI: Gunakan ImageProcessor ---
        // 1. Buat TensorImage dari Bitmap
        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        // 2. Proses gambar (resize, normalize)
        tensorImage = imageProcessor.process(tensorImage)

        // 3. Siapkan buffer output seperti sebelumnya
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, labels.size), DataType.FLOAT32)

        // 4. Jalankan inferensi
        // Sekarang kita menggunakan tensorImage.buffer yang sudah diproses
        interpreter.run(tensorImage.buffer, outputBuffer.buffer)

        // Logika untuk mendapatkan hasil tetap sama
        val result = outputBuffer.floatArray
        val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1

        return if (maxIndex != -1) {
            // Tampilkan hasil dengan format yang lebih mudah dibaca (persentase)
            val confidence = result[maxIndex] * 100
            "${labels[maxIndex]}"
        } else {
            "Unknown"
        }
    }

    // Tambahkan fungsi ini untuk membersihkan resource saat class tidak lagi digunakan
    fun close() {
        interpreter.close()
    }
}