package com.example.opsc7213_goalignite.adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.opsc7213_goalignite.R

class MusicAdapter(
    private val context: Context,
    private val musicList: List<String>, //decalres variables
    private val musicImages: List<Int>
) : BaseAdapter() {

    override fun getCount(): Int = musicList.size //gets lenght of the song

    override fun getItem(position: Int): Any = musicList[position] // places music in a lis

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_music, parent, false)

        val musicImage: ImageView = view.findViewById(R.id.music_image)
        val musicTitle: TextView = view.findViewById(R.id.music_title)

        //displays the music and the image
        musicImage.setImageResource(musicImages[position])
        musicTitle.text = musicList[position]

        return view
    }
}
