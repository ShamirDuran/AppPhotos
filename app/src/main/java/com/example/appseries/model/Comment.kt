package com.example.appseries.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Comment(
    var user_id: String = "",
    var post_id: String = "",
    var content: String = ""
) : Serializable {
    var date: String = getCurrentDate()

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy hh-mm-ss", Locale.ENGLISH)
        return sdf.format(Date())
    }
}
