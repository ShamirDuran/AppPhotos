package com.example.appseries.data

import android.content.ComponentCallbacks
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.appseries.network.Callback


class MessageFactory {
    companion object {
        val TYPE_ADVISE = "typeAdvice"
    }

    fun getDialog(
        context: Context,
        type: String,
        callback: Callback<Boolean>
    ): AlertDialog.Builder {
        when (type) {
            TYPE_ADVISE -> {
                return AlertDialog.Builder(context)
                    .setMessage("Delete this post")
                    .setPositiveButton("yes",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            callback.onSuccess(true)
                        })
                    .setNegativeButton("no",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            callback.onSuccess(false)
                        })
            }
        }
        return AlertDialog.Builder(context)
            .setMessage("Add type alertDialog")
    }
}