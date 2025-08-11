package com.example.jukang.helper

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun AlertOpstion(
    context: Context,
    title: String,
    message: String,
    positive: (DialogInterface) -> Unit = {},
    negative: (DialogInterface) -> Unit = {}
): MaterialAlertDialogBuilder {
    val build = MaterialAlertDialogBuilder(context)
    build.setTitle(title)
    build.setMessage(message)
    build.setPositiveButton("OK") { dialog, _ -> positive(dialog) }
    build.setNegativeButton("Cancel") { dialog, _ -> negative(dialog) }
    return build
}

fun AlertError(
    context: Context,
    title: String,
    message: String,
): MaterialAlertDialogBuilder {
    val build = MaterialAlertDialogBuilder(context)
    build.setTitle(title)
    build.setMessage(message)
    build.setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss()
    }

    return build
}