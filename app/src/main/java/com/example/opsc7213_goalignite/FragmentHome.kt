package com.example.opsc7213_goalignite

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import android.Manifest
import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
//Code adapted deom Android Developers
//Create a Notification by Android Developers
//https://developer.android.com/develop/ui/views/notifications/build-notification

class FragmentHome : Fragment() {

    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var notificationAdapter: ArrayAdapter<Event>
    private val notificationList = mutableListOf<Event>()
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private lateinit var badgeManager: BadgeManager
    private lateinit var badgeAdapter: BadgeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        badgeManager = BadgeManager(requireContext())
        setupRecyclerView(view)

        eventsViewModel = ViewModelProvider(requireActivity()).get(EventsViewModel::class.java)


        createNotificationChannel()
        checkNotificationPermission()


        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, ..., 7 = Saturday
        Log.d("HomeFragment", "Current Day of Week: $currentDayOfWeek")

        badgeManager.setBadgeActive(currentDayOfWeek)

        // Check badges after setting
        val badges = badgeManager.getBadges()
        for (badge in badges) {
            Log.d("HomeFragment", "Badge for day ${badge.dayOfWeek}: ${badge.isActive}")
        }

        badgeAdapter.updateBadges(badges)

        notificationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notificationList)

        // Observe events and check for upcoming events
        eventsViewModel.events.observe(viewLifecycleOwner) { events ->
            Log.d("FragmentHome", "Observed events: $events") // Log observed events
            setupNotifications(events)
        }


        return view
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewBadges)
        badgeAdapter = BadgeAdapter(badgeManager.getBadges())

        // Set the layout manager to horizontal
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = badgeAdapter
    }


    private fun getCurrentDayOfWeek(): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault()) // Use "EEEE" for full day name
        return sdf.format(Date())
    }
//Method to send notification to device
    private fun setupNotifications(events: List<Event>) {
        notificationList.clear() // Clear the notification list to avoid duplicates

        // Filter events that are happening tomorrow and add them to the notification list
        events.forEach { event ->
            if (isEventTomorrow(event.date)) {
                val notificationMessage = "Tomorrow: '${event.name}' at ${event.time}"
                notificationList.add(Event("Upcoming Event", notificationMessage))
                createNotification(event.name, event.time) // Create notification for the system tray
            }
        }

        if (notificationList.isNotEmpty()) {
            notificationAdapter.notifyDataSetChanged()
        } else {
            Log.d("FragmentHome", "No events tomorrow to display.")
        }
    }
//Method to create notification when event is tomorrow
    private fun isEventTomorrow(eventDate: String): Boolean {
        val parsedDate = LocalDate.parse(eventDate, formatter)
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        return parsedDate.isEqual(tomorrow)
    }
//Method to create notification
    private fun createNotification(eventName: String, eventTime: String) {
        val notificationMessage = "Tomorrow: '$eventName' at $eventTime" // Notification message format

        // Create the notification to display in the notification tray
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(requireContext(), "event_channel_id")
            .setContentTitle("Upcoming Event")
            .setContentText(notificationMessage)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Use a unique ID for each notification
        notificationManager.notify("${eventName}-${System.currentTimeMillis()}".hashCode(), notification)
        Log.d("FragmentHome", "Notification created: $notificationMessage")
    }

//Method asks device for notification permision
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            } else {
                Log.d("FragmentHome", "Notification permission already granted.")
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "event_channel_id",
                "Event Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for event notifications"
            }
            val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
