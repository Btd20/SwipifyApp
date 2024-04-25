package com.example.swipifyapp

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(var originalSongs: List<Cursor>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var filteredSongs: List<Cursor> = originalSongs

    fun filter(query: String, songs: List<Cursor>) {
        filteredSongs = if (query.isEmpty()) {
            originalSongs
        } else {
            songs.filter { song ->
                val titleIndex = song.getColumnIndex("titulo")
                val artistIndex = song.getColumnIndex("artista")
                val albumIndex = song.getColumnIndex("album")
                val genreIndex = song.getColumnIndex("genero")

                val title = song.getString(titleIndex)
                val artist = song.getString(artistIndex)
                val album = song.getString(albumIndex)
                val genre = song.getString(genreIndex)

                title.contains(query, ignoreCase = true) ||
                        artist.contains(query, ignoreCase = true) ||
                        album.contains(query, ignoreCase = true) ||
                        genre.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val song = filteredSongs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return filteredSongs.size
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)
        private val albumTextView: TextView = itemView.findViewById(R.id.albumTextView)
        private val genreTextView: TextView = itemView.findViewById(R.id.genreTextView)

        fun bind(song: Cursor) {
            val titleIndex = song.getColumnIndex("titulo")
            val artistIndex = song.getColumnIndex("artista")
            val albumIndex = song.getColumnIndex("album")
            val genreIndex = song.getColumnIndex("genero")

            titleTextView.text = song.getString(titleIndex)
            artistTextView.text = song.getString(artistIndex)
            albumTextView.text = song.getString(albumIndex)
            genreTextView.text = song.getString(genreIndex)
        }
    }
}

