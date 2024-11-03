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
                "Does the app have a dark mode?",
                "Yes, the app supports dark mode! You can enable it in the \"Settings\" menu under \"Display Preferences\" to reduce eye strain during late-night study sessions."
            ),
            com.example.opsc7213_goalignite.FAQItem(
                "What if I have further questions not covered in the FAQ?",
                "If you have further questions, feel free to reach out to our support team through the \"Contact Us\" section in the app. We’re here to help!"
            ),
            com.example.opsc7213_goalignite.FAQItem(
                "Can I access the app offline?",
                "Yes, you can access certain features of the app offline. However, to sync your progress and download study materials, you will need an internet connection."
            ),
            com.example.opsc7213_goalignite.FAQItem(
                "What features does the app offer?",
                "No, there are no limits on the number of exports."
            ),
            com.example.opsc7213_goalignite.FAQItem(
                "What should I do if I encounter bugs or technical issues?",
                "If you encounter any bugs or technical issues, please report them through the supportform section. Provide as much detail as possible for a quicker resolution."
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