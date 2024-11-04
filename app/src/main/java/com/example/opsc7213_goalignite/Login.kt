package com.example.opsc7213_goalignite

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.opsc7213_goalignite.databinding.ActivityLoginBinding
import com.facebook.appevents.codeless.internal.EventBinding
import java.util.Calendar
import java.util.concurrent.Executor

//Login and Register code taken from GeeksforGeeks
//https://www.geeksforgeeks.org/login-and-registration-in-android-using-firebase-in-kotlin/
//ayus-Login and Registration in Android using Firebase in Kotlin(2022)
class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var lEmail: EditText
    private lateinit var lPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var biometric : ImageView
    private lateinit var binding: ActivityLoginBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var info: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()



        lEmail = findViewById(R.id.lEmail)
        lPassword = findViewById(R.id.lPassword)
        btnLogin = findViewById(R.id.btnLogin)
        biometric = binding.biometric

        val rLogin = findViewById<TextView>(R.id.rLogin)

        btnLogin.setOnClickListener {
            loginUser()
        }

        biometric.setOnClickListener{
            checkDeviceHasBiometric()
            biometricPrompt.authenticate(promptInfo)
        }
        rLogin.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this@Login, MainActivity::class.java))
                finish()
                // Check if the user is already signed in
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // If the user is authenticated, proceed to the main activity
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    finish() // End the Login activity
                } else {
                    // If not, prompt the user to log in with email and password
                    Toast.makeText(applicationContext, "Please log in with email and password.", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for Goal Ignite")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

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

    private fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                info = "App can authenticate using biometrics."
                biometric.isEnabled = true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                info = "No biometric features available on this device."
                biometric.isEnabled = false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                info = "Biometric features are currently unavailable."
                biometric.isEnabled = false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("MY_APP_TAG", "No biometric credentials enrolled.")
                info = "No biometric credentials enrolled. Please enroll and try again."
                // Use ActivityResultLauncher to handle biometric enrollment intent
                biometric.isEnabled = false
                enrollmentLauncher.launch(Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                })
            }
        }
        binding.tvShowMsg.text = info
    }

    // Register for biometric enrollment result
    private val enrollmentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            checkDeviceHasBiometric() // Re-check biometric capability after enrollment
        } else {
            Toast.makeText(this, "Enrollment failed or canceled", Toast.LENGTH_SHORT).show()
        }
    }
}
