package com.example.opsc7213_goalignite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

//Login and Register code taken from GeeksforGeeks
//https://www.geeksforgeeks.org/login-and-registration-in-android-using-firebase-in-kotlin/
//ayus-Login and Registration in Android using Firebase in Kotlin(2022)
//Code adapted from Firebase
//https://firebase.google.com/docs/auth/android/facebook-login#:~:text=You%20can%20let%20your%20users%20authenticate%20with%20Firebase%20using
//Fitrebase-Authenticate Using Facebook Login on Android
class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //firebase authentication instance
    private lateinit var db: FirebaseFirestore //firebase database instance
    private lateinit var callbackManager: CallbackManager // Callback manager for Facebook login
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        callbackManager = CallbackManager.Factory.create()

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val rName = findViewById<EditText>(R.id.rName)
        val rEmail = findViewById<EditText>(R.id.rEmail)
        val rPassword = findViewById<EditText>(R.id.rPassword)
        val rCPassword = findViewById<EditText>(R.id.rCPassword)
        val rLogin = findViewById<TextView>(R.id.rLogin)

        val rFacebook = findViewById<ImageView>(R.id.rFacebook)


        rFacebook.setOnClickListener {
            signInWithFacebook()// Initiate Facebook Sign-In
        }
        // Redirect to Login activity
        rLogin.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }
        // Register button click listener
        btnRegister.setOnClickListener {
            val name = rName.text.toString().trim()
            val email = rEmail.text.toString().trim()
            val password = rPassword.text.toString().trim()
            val confirmPassword = rCPassword.text.toString().trim()

            rName.text.clear()
            rEmail.text.clear()
            rPassword.text.clear() // Clear the input fields
            rCPassword.text.clear()

            // Validation checks
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register user with Firebase Authentication
            registerUser(name, email, password)

        }
    }

    // Method to initiate Facebook Sign-In
    private fun signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile")) // Request permissions
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result?.accessToken) // Handle access token
                val intent = Intent(
                    this@Register,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish() // This will close the Register activity

            }

            override fun onCancel() {
                Toast.makeText(this@Register, "Facebook login canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(this@Register, "Facebook login failed: ${error?.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    // Handle Facebook access token and authenticate with Firebase
    private fun handleFacebookAccessToken(token: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(token?.token!!)// Gets credential
        auth.signInWithCredential(credential) // Sign in with credential
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = hashMapOf(
                        "name" to auth.currentUser?.displayName,
                        "email" to auth.currentUser?.email
                    )
                    //save facebook user info on firestore
                    db.collection("users").document(auth.currentUser?.uid!!)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Facebook Sign-In successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving user info: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Facebook Sign-In failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Handle the result of sign-in activities
    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email
                    )
                    db.collection("users").document(auth.currentUser?.uid!!)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error adding document: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}