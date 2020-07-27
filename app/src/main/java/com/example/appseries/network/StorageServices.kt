package com.example.appseries.network

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

class StorageServices {
    val storageRef = FirebaseStorage.getInstance()

    fun uploadImageToFirebase(filename:String, uri:Uri?): String{

        var url:String = ""
        val ref = storageRef.getReference("/images/$filename")
        ref.putFile(uri!!)
            .addOnSuccessListener {
                Log.d("StorageServices", "Succesfully uploaded image ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("StorageServices", "File location: ${it.toString()}")
                    url = it.toString()
                }

                ref.downloadUrl.addOnFailureListener{
                    Log.w("StorageServices", "Error get file location", it)
                }
            }

        return url
    }
}