package com.example.appseries.network

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

class StorageServices {
    val storageRef = FirebaseStorage.getInstance()

    fun uploadImageToFirebase(filename:String, uri:Uri?){
        val ref = storageRef.getReference("/images/$filename")
        ref.putFile(uri!!)
            .addOnSuccessListener {
                Log.d("StorageServices", "Succesfully uploaded image ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("StorageServices", "File location: ${it.toString()}")
                }
            }
    }
}