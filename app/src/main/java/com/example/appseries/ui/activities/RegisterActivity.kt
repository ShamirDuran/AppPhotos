package com.example.appseries.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.example.appseries.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterUser"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        btReturnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btRegister.setOnClickListener {
            validateRegister()
        }
    }

    private fun registerUser() {
        auth.createUserWithEmailAndPassword(edUsername.text.toString(), edPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val auth = FirebaseAuth.getInstance()
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "createUserWithEmail:success")
                                Log.d(TAG, "Email sent.")
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Register failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validateRegister() {
        if (edUsername.text.toString().isEmpty()) {
            edUsername.error = "Please enter email"
            edUsername.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(edUsername.text.toString()).matches()) {
            edUsername.error = "Not a valid email"
            edUsername.requestFocus()
            return
        }

        if (edPassword.text.toString().isEmpty()) {
            edPassword.error = "Please enter password"
            edPassword.requestFocus()
            return
        }

        if (edRePassword.text.toString().isEmpty()) {
            edRePassword.error = "Please repeat password"
            edRePassword.requestFocus()
            return
        }

        if (edRePassword.text.toString() != edPassword.text.toString()) {
            edRePassword.error = "Password must match"
            edRePassword.requestFocus()
            return
        }

        registerUser()
    }
}