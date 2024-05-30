package com.example.swipifyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListsFragmentTest {
    @Test
    fun testPlaylistAdapter() {
        // Lista de nombres de listas de reproducción para probar el adaptador
        val playlists = listOf("Playlist1", "Playlist2", "Playlist3")
        val adapter = PlaylistAdapter(playlists, "UsuarioPrueba")

        // Verifica si el número de elementos en el adaptador coincide con el tamaño de la lista de reproducción
        assertEquals(playlists.size, adapter.itemCount)

        // Verifica si el primer elemento del adaptador coincide con el primer elemento de la lista de reproducción
        val firstItem = playlists[0]
        // Crear una instancia de PlaylistAdapter

// Crear una vista dummy para usarla en el viewHolder
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dummyView = LayoutInflater.from(context).inflate(R.layout.playlist_item, null)

// Crear una instancia de PlaylistViewHolder
        val viewHolder = adapter.PlaylistViewHolder(dummyView)

        adapter.onBindViewHolder(viewHolder, 0)
        assertEquals(firstItem, viewHolder.playlistNameTextView.text.toString())
    }

}

