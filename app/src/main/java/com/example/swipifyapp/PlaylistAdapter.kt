package com.example.swipifyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaylistAdapter(private var playlists: List<String> = emptyList(), private val username: String?) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlistName = playlists[position]
        holder.bind(playlistName, username)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun submitList(newList: List<String>) {
        playlists = newList
        notifyDataSetChanged()
    }

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playlistNameTextView: TextView = itemView.findViewById(R.id.playlistNameTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.author)

        fun bind(playlistName: String, username: String?) {
            playlistNameTextView.text = playlistName
            username?.let {
                authorTextView.text = "Creada por: " + it
            }
        }
    }
}

