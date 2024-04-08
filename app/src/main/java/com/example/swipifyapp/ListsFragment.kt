package com.example.swipifyapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListsFragment : Fragment() {

    private lateinit var btnCrearLista: Button
    private lateinit var recyclerViewPlaylists: RecyclerView
    private lateinit var playlistsAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lists, container, false)
        btnCrearLista = view.findViewById(R.id.btnCrearLista)
        recyclerViewPlaylists = view.findViewById(R.id.recyclerViewPlaylists)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerViewPlaylists.layoutManager = LinearLayoutManager(context)
        val sharedPreferences = requireContext().getSharedPreferences("swipify_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        playlistsAdapter = PlaylistAdapter(emptyList(), username) // Inicializamos con una lista vacía y el nombre de usuario
        recyclerViewPlaylists.adapter = playlistsAdapter

        // Obtener listas de reproducción de la base de datos
        val dbHandler = SwipifyDatabase(requireContext())
        val playlistsFromDB = dbHandler.getAllPlaylists(requireContext())

        // Agregar listas de reproducción al adaptador
        playlistsAdapter.submitList(playlistsFromDB)

        // Button click listener for creating playlist
        btnCrearLista.setOnClickListener {
            showCreatePlaylistDialog()
        }
    }


    private fun showCreatePlaylistDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_create_playlist, null)
        val etPlaylistName = dialogView.findViewById<EditText>(R.id.etPlaylistName)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Crear lista de reproducción")
            .setPositiveButton("Crear") { dialog, which ->
                val playlistName = etPlaylistName.text.toString()
                addPlaylist(playlistName)
            }
            .setNegativeButton("Cancelar", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun addPlaylist(name: String) {
        // Add playlist to the database
        val dbHandler = SwipifyDatabase(requireContext())
        val result = dbHandler.insertPlaylist(requireContext(), name, "")

        if (result != -1L) {
            // If playlist was added successfully to the database
            // Update the RecyclerView
            val playlistsFromDB = dbHandler.getAllPlaylists(requireContext())
            playlistsAdapter.submitList(playlistsFromDB)
            Toast.makeText(context, "Lista de reproducción '$name' creada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error al crear la lista de reproducción", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_IMAGE = 100
    }
}