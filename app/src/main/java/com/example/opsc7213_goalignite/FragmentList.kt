package com.example.opsc7213_goalignite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7213_goalignite.adapter.ToDoAdapter
import com.example.opsc7213_goalignite.model.ToDoModel
import com.example.opsc7213_goalignite.utilis.DatabaseHandler
import com.example.opsc7213_goalignite.utilis.NetworkUtil.isNetworkAvailable
import com.google.android.material.floatingactionbutton.FloatingActionButton

//THIS WHOLE CODE WAS TAKEN FROM YOUTUBE
//https://www.youtube.com/watch?v=7u5_NNrbQos&list=PLzEWSvaHx_Z2MeyGNQeUCEktmnJBp8136
//Penguin Coders - TO-DO-LIST APPLICATION

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentList.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentList : Fragment(), DialogCloseListener {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private lateinit var fab: FloatingActionButton
    private var taskList: MutableList<ToDoModel> = mutableListOf()
    private lateinit var db: DatabaseHandler

    private var networkChangeReceiver: NetworkChangeReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHandler(requireContext()) // Use requireContext() here
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Initialize the RecyclerView
        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext()) // Use requireContext() here
        tasksAdapter = ToDoAdapter(db, requireActivity()) // Use requireActivity() here
        tasksRecyclerView.adapter = tasksAdapter

        fab = view.findViewById(R.id.fab) // Use view.findViewById here

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(requireContext(), tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        // Fetch tasks from the database and update the adapter
        taskList = db.getAllTasks().toMutableList()
        taskList.reverse()
        tasksAdapter.setTasks(taskList)

        // Handle FloatingActionButton click
        fab.setOnClickListener {
            if (isNetworkAvailable(requireContext())) {
                AddNewTask().show(parentFragmentManager, AddNewTask.TAG) // Show the dialog to add a new task
            } else {
                Toast.makeText(requireContext(), "Tasks will sync when the network is online.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // Initialize and register the networkChangeReceiver
        networkChangeReceiver = NetworkChangeReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        // Only unregister the receiver if it was initialized
        networkChangeReceiver?.let {
            requireContext().unregisterReceiver(it)
            networkChangeReceiver = null // Optional: set to null after unregistering
        }
    }

    override fun handleDialogClose(dialog: DialogInterface) {
        taskList = db.getAllTasks().toMutableList()
        taskList.reverse()
        tasksAdapter.setTasks(taskList)
        tasksAdapter.notifyItemInserted(0)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
