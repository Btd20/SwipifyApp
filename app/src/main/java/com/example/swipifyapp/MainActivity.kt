package com.example.swipifyapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playPauseButton: ImageButton
    private lateinit var restartButton: ImageButton
    private lateinit var musicTitleTextView: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var profileImageView: ImageView
    private var isPlaying = false

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_discover -> {
                    replaceFragment(ExploreFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_lists -> {
                    replaceFragment(ListsFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    replaceFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playPauseButton = findViewById(R.id.playPauseButton)
        restartButton = findViewById(R.id.restartButton)
        mediaPlayer = MediaPlayer.create(this, R.raw.onceyonce)
        musicTitleTextView = findViewById(R.id.musicTitleTextView)
        seekBar = findViewById(R.id.seekBar)
        profileImageView = findViewById(R.id.imageView4)

        playPauseButton.setOnClickListener {
            togglePlayPause()
        }

        restartButton.setOnClickListener {
            restartMusic()
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Cargar el primer fragmento al iniciar la actividad
        bottomNavigationView.selectedItemId = R.id.navigation_home

        // Configurar el SeekBar
        seekBar.max = mediaPlayer.duration
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No se necesita implementar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No se necesita implementar
            }
        })

        // Actualizar la posición del SeekBar mientras se reproduce el audio
        Thread {
            while (mediaPlayer != null) {
                try {
                    runOnUiThread {
                        seekBar.progress = mediaPlayer.currentPosition
                    }
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()

        // Configurar el OnClickListener para la imagen de perfil
        profileImageView.setOnClickListener {
            val intent = Intent(this, ReportsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            pauseMusic()
        } else {
            playMusic()
        }
    }

    private fun playMusic() {
        isPlaying = true
        playPauseButton.setBackgroundResource(R.drawable.pause) // Establecer imagen de pausa
        mediaPlayer.start() // Iniciar la reproducción del archivo de audio
        musicTitleTextView.visibility = TextView.VISIBLE // Mostrar el texto
        musicTitleTextView.isSelected = true
    }

    private fun pauseMusic() {
        isPlaying = false
        playPauseButton.setBackgroundResource(R.drawable.play) // Establecer imagen de reproducción
        mediaPlayer.pause() // Pausar la reproducción del archivo de audio
    }

    private fun restartMusic() {
        mediaPlayer.seekTo(0) // Reiniciar la canción al principio
        if (!isPlaying) {
            playMusic() // Si la canción no estaba reproduciendo, iniciar la reproducción
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Liberar recursos del reproductor de medios cuando se destruye la actividad
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
