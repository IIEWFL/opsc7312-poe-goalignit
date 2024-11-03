package com.example.opsc7213_goalignite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7213_goalignite.adapter.GradeAdapter
import com.example.opsc7213_goalignite.model.GradeModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


//This whole code was taken from YOUTUBE
//https://www.youtube.com/watch?v=4ZFde7We0H8&list=PLNSnaPe-iLVKpRUqg1yv5JqM_4DQN8Ofo
// Jowel Ahmed - CONTACT APPLICATION BUT CHANGED AS THE "ADD GRADE"

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragmentGrades : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var fab3: FloatingActionButton
    private lateinit var gradeRv: RecyclerView
    private lateinit var databaseHP: DatabaseHP
    private lateinit var gradeAdapter: GradeAdapter



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
        val view = inflater.inflate(R.layout.fragment_grades, container, false)


        databaseHP = DatabaseHP(requireContext())

        gradeRv = view.findViewById(R.id.gradeRv)
        gradeRv.setHasFixedSize(true)
        loadData()

        // Initialization of FloatingActionButton
        fab3 = view.findViewById(R.id.fab3)

        // Set OnClickListener for the FloatingActionButton
        fab3.setOnClickListener {
            // Start the AddEditGrade Activity when the button is clicked
            val intent = Intent(activity, AddEditGrade::class.java)
            startActivity(intent)
        }

        return view
    }
    private fun loadData() {

        val gradeList: ArrayList<GradeModel> = databaseHP.getAllData()

        // Initialize the adapter with the fetched data
        gradeAdapter = GradeAdapter(requireContext(), gradeList)
        gradeRv.adapter = gradeAdapter
    }

    override fun onResume() {
        super.onResume()
        loadData()  // Call loadData to refresh contact list when fragment resumes
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentCalendar.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCalendar().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

