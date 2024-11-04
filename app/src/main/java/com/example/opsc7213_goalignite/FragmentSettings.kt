package com.example.opsc7213_goalignite


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import java.util.Locale
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSettings.newInstance] factory method to
 * create an instance of this fragment.
 */
//Code adapted from Medium
//https://medium.com/@myofficework000/managing-user-authentication-and-data-with-firestore-in-android-using-jetpack-compose-6fb4da2e01e5#:~:text=In%20this%20comprehensive%20tutorial,%20we%20will%20guide%20you%20through
//Abhishek Pathak Managing User Authentication and Data with Firestore in Android using Jetpack Compose(2023)
class FragmentSettings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var themeLayouts: LinearLayout
    private lateinit var themeLayout: LinearLayout
    private lateinit var switchLayout: LinearLayout
    private lateinit var themeArrow: ImageView //Initializes variables
    private lateinit var switchMode: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var changeMyLanguage: Button
    private lateinit var languageSwitch: LinearLayout
    private lateinit var layoutSwitch: LinearLayout
    private lateinit var supportLayout: LinearLayout
    private lateinit var dropdownLayout: LinearLayout
    private lateinit var supportFormLayout: LinearLayout

    private var nightMode: Boolean = false


    private lateinit var profileArrow: ImageView
    private lateinit var profilelayout: LinearLayout   //Initializes variables
    private lateinit var profileFieldsLayout: LinearLayout
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveProfileButton: Button


    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize views
        themeLayout = view.findViewById(R.id.themeLayout)
        switchLayout = view.findViewById(R.id.switchLayout)
        themeArrow = view.findViewById(R.id.themearrow)
        switchMode = view.findViewById(R.id.switchMode)
        changeMyLanguage = view.findViewById(R.id.changeMyLanguage)
        layoutSwitch = view.findViewById(R.id.LayoutSwitch)
        dropdownLayout = view.findViewById(R.id.dropdownLayout)
        supportLayout = view.findViewById(R.id.supportLayout)
        languageSwitch = view.findViewById(R.id.LanguageSwitch)
        themeLayouts = view.findViewById(R.id.themeLayouts)
        supportFormLayout = view.findViewById(R.id.supportFormLayout)

        // Initialize SharedPreferences
        sharedPreferences =
            requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        // Load saved theme preference
        val nightMode = sharedPreferences.getBoolean("nightMode", false)
        switchMode.isChecked = nightMode
        updateTheme(nightMode)


        themeLayouts.setOnClickListener {
            // Replace the current fragment with FAQFragment
            replaceWithFAQFragment()
        }

        supportFormLayout.setOnClickListener {
            //replace with current fragment with supportForm
            replaceWithSupportForm()
        }

        profileArrow = view.findViewById(R.id.profilearrow)
        profilelayout = view.findViewById(R.id.profilelayout)
        profileFieldsLayout = view.findViewById(R.id.profileFieldsLayout)
        nameEditText = view.findViewById(R.id.nameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        saveProfileButton = view.findViewById(R.id.saveProfileButton)

        saveProfileButton.setOnClickListener {
            saveProfileData()
            try {
                sendMessage()
            } catch (e: Exception) {
                // Handle the error gracefully, logging it and notifying the user
                handleError(e)
            }
        }
        loadUserData()

        switchMode = view.findViewById(R.id.switchMode)

        // Initialize SharedPreferences
        sharedPreferences =
            requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()


        // Set click listener for the theme layout
        themeLayout.setOnClickListener {
            if (switchLayout.visibility == View.GONE) {
                switchLayout.visibility = View.VISIBLE
                themeArrow.rotation = 90f // Rotate arrow to indicate expansion
            } else {
                switchLayout.visibility = View.GONE
                themeArrow.rotation = 0f // Reset arrow rotation
            }
        }
        profilelayout.setOnClickListener {
            if (profileFieldsLayout.visibility == View.GONE) {
                profileFieldsLayout.visibility = View.VISIBLE
                profileArrow.rotation = 90f // Rotate arrow to indicate expansion
            } else {
                profileFieldsLayout.visibility = View.GONE
                profileArrow.rotation = 0f // Reset arrow rotation
            }

        }

        // Set listener for the switch to toggle theme
        switchMode.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("nightMode", isChecked)
            editor.apply()
            updateTheme(isChecked)
        }


        supportLayout.setOnClickListener {
            toggleDropdown()
        }

        // Set the click listener for change language button
        languageSwitch.setOnClickListener {
            toggleLayoutSwitch()
        }

        changeMyLanguage.setOnClickListener {
            // Code to show language options
            showChangeLanguageDialog()
        }

        return view

    }

    private fun sendMessage() {
        try {
            // Attempt to display a Toast message
            Toast.makeText(
                context,
                "Your profile update request has been sent!",
                Toast.LENGTH_SHORT
            ).show()

        } catch (e: Exception) {
            // Handle any errors when trying to display the message
            throw Exception("Failed to send message: ${e.localizedMessage}")
        }
    }


    private fun handleError(e: Exception) {
        // Log the error for debugging purposes
        Log.e("ProfileManagement", "Error: ${e.localizedMessage}")

        // Show an error message to the user
        Toast.makeText(context, "Error sending message: ${e.localizedMessage}", Toast.LENGTH_LONG)
            .show()
    }

    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            // Load the email from FirebaseAuth
            emailEditText.setText(user.email)

            // Load the name from FirebaseAuth or Firestore
            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        nameEditText.setText(name)
                    }
                }
        }
    }

    private fun saveProfileData() {
        val updatedName = nameEditText.text.toString() //takes in new profile data
        val updatedEmail = emailEditText.text.toString()
        val updatedPassword = passwordEditText.text.toString()

        val user = auth.currentUser

        user?.let {
            // Update email
            if (updatedEmail.isNotEmpty() && updatedEmail != user.email) {
                user.updateEmail(updatedEmail).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(context, "Failed to update email", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Update password
            if (updatedPassword.isNotEmpty()) {
                user.updatePassword(updatedPassword).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            // Update name in Firestore
            val userProfileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(updatedName)
                .build()

            user.updateProfile(userProfileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save name to Firestore
                    val userData = mapOf(
                        "name" to updatedName,
                        "email" to updatedEmail
                    )
                    firestore.collection("users").document(user.uid).set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Profile updated successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to replace the current fragment with the FAQ fragment
    private fun replaceWithFAQFragment() {
        // Create an instance of the FAQ fragment
        val fragment = FragmentFaq()
        // Begin a transaction to replace the existing fragment in the specified container
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, // Replace 'fragment_container' with your actual container ID
                fragment
            )
            .addToBackStack(null) // Add the transaction to the back stack for navigation
            .commit() // Commit the transaction to apply the changes
    }

    // Function to replace the current fragment with the Support Form fragment
    private fun replaceWithSupportForm() {
        // Create an instance of the Support Form fragment
        val supportFormFragment = SupportForm()
        // Begin a transaction to replace the existing fragment in the specified container
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, // Use the correct container ID here
                supportFormFragment
            )
            .addToBackStack(null) // Add the transaction to the back stack for navigation
            .commit() // Commit the transaction to apply the changes
    }

    // Function to toggle the visibility of a layout with ID 'LayoutSwitch'
    private fun toggleLayoutSwitch() {
        // Find the layout with the ID 'LayoutSwitch' within the current view
        val layoutSwitch = view?.findViewById<LinearLayout>(R.id.LayoutSwitch)
        // Toggle the visibility: if it's currently GONE, make it VISIBLE, otherwise set it to GONE
        if (layoutSwitch?.visibility == View.GONE) {
            layoutSwitch.visibility = View.VISIBLE
        } else {
            layoutSwitch?.visibility = View.GONE
        }
    }

    // Function to toggle the visibility of the dropdown layout
    private fun toggleDropdown() {
        // Toggle the visibility: if the dropdown layout is currently GONE, make it VISIBLE, otherwise set it to GONE
        if (dropdownLayout.visibility == View.GONE) {
            dropdownLayout.visibility = View.VISIBLE
        } else {
            dropdownLayout.visibility = View.GONE
        }
    }


    private fun showChangeLanguageDialog() {
        // Languages to display
        val listItems = arrayOf("isiZulu", "English", "Sesotho")

        // Create AlertDialog
        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle("Choose language...")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
            when (i) {
                0 -> {
                    // Zulu
                    setLocale("zu")
                    saveLanguageToPreferences("zu")  // Save the language selection
                    reloadCurrentFragment()
                }

                1 -> {
                    // English
                    setLocale("en")
                    saveLanguageToPreferences("en")
                    reloadCurrentFragment()// Save the language selection

                }

                2 -> {
                    setLocale("st")
                    saveLanguageToPreferences("st")
                    reloadCurrentFragment()// Save the language selection

                }
            }


            // Dismiss the alert dialog once a language is selected
            dialogInterface.dismiss()
        }


        // Create and show the dialog
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    // Function to reload the current fragment by detaching and re-attaching it
    private fun reloadCurrentFragment() {
        // Begin a new fragment transaction using the parent fragment manager
        parentFragmentManager.beginTransaction().apply {
            // Detach the current instance of FragmentSettings to remove it from the view
            detach(this@FragmentSettings)
            // Re-attach the same instance of FragmentSettings to reload it
            attach(this@FragmentSettings)
            // Commit the transaction to apply the changes and refresh the fragment
            commit()
        }
    }


    // Function to change the app's language/locale based on the given language code
    private fun setLocale(languageCode: String) {
        // Create a Locale object with the specified language code (e.g., "en" for English)
        val locale = Locale(languageCode)
        // Set this locale as the default for the app
        Locale.setDefault(locale)
        // Get the current configuration of the app's resources
        val config = requireContext().resources.configuration
        // Update the configuration with the new locale
        config.setLocale(locale)
        // Apply the new configuration and update the display metrics to reflect the language change
        requireContext().resources.updateConfiguration(
            config,
            requireContext().resources.displayMetrics
        )
    }

    // Function to save the selected language code to SharedPreferences for persistence
    private fun saveLanguageToPreferences(languageCode: String) {
        // Retrieve the SharedPreferences instance with the name "Settings" in private mode
        val sharedPref = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        // Open an editor to make changes to SharedPreferences
        with(sharedPref.edit()) {
            // Store the selected language code under the key "selected_language"
            putString("selected_language", languageCode)
            // Apply the changes asynchronously to save the selected language
            apply()
        }
    }

    private fun loadLanguageFromPreferences() {
        // Load the saved language from SharedPreferences
        val sharedPref =
            requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        val languageCode =
            sharedPref.getString("selected_language", "en")  // Default to English if not set
        setLocale(languageCode!!)
    }


    // Update theme based on the user preference
    private fun updateTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    // Update theme based on the user preference


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSettings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSettings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
