package com.example.jukang.helper.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun createMultipartFromFile(file: File): MultipartBody.Part {
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("photo", file.name, requestFile)
}

fun uriToFile(context: Context, uri: Uri): File {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val file = File.createTempFile("upload", ".jpg", context.cacheDir)
    val outputStream = file.outputStream()
    inputStream?.copyTo(outputStream)
    outputStream.close()
    inputStream?.close()
    return file
}

