package com.example.opsc7213_goalignite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.Calendar

//Login and Register code taken from GeeksforGeeks
//https://www.geeksforgeeks.org/login-and-registration-in-android-using-firebase-in-kotlin/
//ayus-Login and Registration in Android using Firebase in Kotlin(2022)
class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var lEmail: EditText
    private lateinit var lPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var sharedViewModel: BadgeManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
            promptBiometricAuthentication()
        } else {
            Toast.makeText(this, "Biometric authentication is not available.", Toast.LENGTH_LONG).show()
        }

        lEmail = findViewById(R.id.lEmail)
        lPassword = findViewById(R.id.lPassword)
        btnLogin = findViewById(R.id.btnLogin)

        val rLogin = findViewById<TextView>(R.id.rLogin)

        btnLogin.setOnClickListener {
            loginUser()
        }
        rLogin.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun promptBiometricAuthentication() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Navigate to MainActivity after successful authentication
                startActivity(Intent(this@Login, MainActivity::class.java))
                finish()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for Goal Ignite")
            .setSubtitle("Log in using your fingerprint")
            .setNegativeButtonText("Use account password")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun loginUser() {
        val email = lEmail.text.toString().trim()
        val password = lPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success, redirect to the main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    // If login fails, display a message to the user.
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


}
