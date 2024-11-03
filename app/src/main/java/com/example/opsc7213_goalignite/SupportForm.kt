package com.example.opsc7213_goalignite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 * Use the [SupportForm.newInstance] factory method to
 * create an instance of this fragment.
 */
class SupportForm : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_form, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Find the button by its ID and set an onClickListener
        val sendButton: Button = view.findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            // Display a Toast message when the button is clicked
            Toast.makeText(
                requireContext(),
                "Your support request has been submitted. We'll be in touch soon:)!!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SupportForm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SupportForm().apply {
                arguments = Bundle().apply {

                }
            }
    }
}