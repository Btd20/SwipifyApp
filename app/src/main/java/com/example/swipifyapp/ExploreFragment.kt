import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.swipifyapp.R
import com.example.swipifyapp.SwipifyDatabase

class ExploreFragment : Fragment() {

    private lateinit var songTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        songTextView = view.findViewById(R.id.song) // Obtener referencia al TextView de la canción
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val likeButton = view.findViewById<Button>(R.id.like)
        likeButton.setOnClickListener {
            showLikeItemDialog()
        }
    }

    private fun showLikeItemDialog() {
        val dbHandler = SwipifyDatabase(requireContext())
        val playlistsFromDB = dbHandler.getAllPlaylists(requireContext())

        if (playlistsFromDB.isNotEmpty()) {
            val playlistNames = playlistsFromDB.map { it }

            val dialogView = layoutInflater.inflate(R.layout.like_item, null)

            val playlistSpinner = dialogView.findViewById<Spinner>(R.id.playlistSpinner)
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, playlistNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            playlistSpinner.adapter = adapter

            val addToPlaylistButton = dialogView.findViewById<Button>(R.id.addToPlaylistButton)
            addToPlaylistButton.setOnClickListener {

                val selectedPlaylist = playlistSpinner.selectedItem as String

                val songName = songTextView.text.toString()


                val result = dbHandler.addToPlaylist(requireContext(), selectedPlaylist, songName)

                if (result != -1L) {

                    Toast.makeText(requireContext(), "Canción agregada a la playlist '$selectedPlaylist'", Toast.LENGTH_SHORT).show()
                } else {
                    // Si hubo un error al agregar la canción a la playlist
                    Toast.makeText(requireContext(), "Error al agregar la canción a la playlist", Toast.LENGTH_SHORT).show()
                }
            }

            // Crear un diálogo de alerta para mostrar el "like_item"
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("It's a Match")
                .setNegativeButton("Cerrar") { dialog, _ ->
                    dialog.dismiss()
                }

            // Crear y mostrar el diálogo
            val dialog = dialogBuilder.create()
            dialog.show()
        } else {
            // Si no hay playlists disponibles, mostrar un mensaje de error
            Toast.makeText(requireContext(), "No hay playlists disponibles", Toast.LENGTH_SHORT).show()
        }
    }
}
