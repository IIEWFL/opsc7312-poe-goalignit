package com.example.opsc7213_goalignite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7213_goalignite.adapter.FAQAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFaq.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFaq : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var recyclerView: RecyclerView
    private lateinit var faqAdapter: FAQAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faq, container, false)
        val faqList = listOf(
            com.example.opsc7213_goalignite.FAQItem(
                "What is the difference between the Free and Paid versions?",
                "The Free version offers basic features while the Paid version includes advanced functionalities."
            ),
            com.example.opsc7213_goalignite.FAQItem(
                "What email systems (ESPs) does the app work with?",
                "The app is compatible with Gmail, Yahoo, Outlook, and more."
            ),
            com.example.opsc7213_goalignite.FAQItem(
                "Do you plan on adding more blocks to the app?",
                "Yes, we are continuously working on adding new features and improvements."
            ),
            com.example.opsc7213_goalignite.FAQItem(
                "Are there any limits to the number of exported files?",
                "No, there are no limits on the number of exports."
            ),
            com.example.opsc7213_goalignite.FAQItem(
                "Can I customize the app settings?",
                "Yes, the app allows you to customize various settings as per your needs."
            )
        )

        recyclerView = view.findViewById(R.id.recyclerView)
        faqAdapter = FAQAdapter(faqList)
        recyclerView.adapter = faqAdapter

        return view
    }


    companion object {


    }
}