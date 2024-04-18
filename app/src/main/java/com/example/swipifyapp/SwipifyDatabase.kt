package com.example.swipifyapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SwipifyDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "SwipifyDatabase.db"

        // Definición de las tablas y sus columnas
        private const val TABLE_USUARIOS = "usuarios"
        private const val TABLE_CANCIONES = "canciones"
        private const val TABLE_LIKES = "likes"
        private const val TABLE_PLAYLISTS = "playlists"

        // Columnas de la tabla usuarios
        private const val KEY_ID = "id"
        private const val KEY_USUARIO = "usuario"
        private const val KEY_CONTRASENYA = "contrasenya"

        // Columnas de la tabla canciones
        private const val KEY_TITULO = "titulo"
        private const val KEY_ARTISTA = "artista"
        private const val KEY_ALBUM = "album"
        private const val KEY_GENERO = "genero"
        private const val KEY_DURACION = "duracion"

        // Columnas de la tabla likes
        private const val KEY_USUARIO_ID = "usuario_id"
        private const val KEY_CANCION_ID = "cancion_id"

        // Columnas de la tabla playlists
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_CANCIONES = "canciones"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Creación de las tablas
        val CREATE_TABLE_USUARIOS = ("CREATE TABLE $TABLE_USUARIOS ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_USUARIO TEXT UNIQUE, $KEY_CONTRASENYA TEXT)")
        val CREATE_TABLE_CANCIONES = ("CREATE TABLE $TABLE_CANCIONES ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_TITULO TEXT, $KEY_ARTISTA TEXT, $KEY_ALBUM TEXT, $KEY_GENERO TEXT, $KEY_DURACION INTEGER)")
        val CREATE_TABLE_LIKES = ("CREATE TABLE $TABLE_LIKES ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_USUARIO_ID INTEGER, $KEY_CANCION_ID INTEGER)")
        val CREATE_TABLE_PLAYLISTS = ("CREATE TABLE $TABLE_PLAYLISTS ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_NOMBRE TEXT UNIQUE, $KEY_CANCIONES TEXT)")
        db.execSQL(CREATE_TABLE_USUARIOS)
        db.execSQL(CREATE_TABLE_CANCIONES)
        db.execSQL(CREATE_TABLE_LIKES)
        db.execSQL(CREATE_TABLE_PLAYLISTS)

        val queryInsertExitosEspana = "INSERT INTO $TABLE_PLAYLISTS ($KEY_NOMBRE, $KEY_CANCIONES) VALUES ('Exitos de España', 'Canción 1,Canción 2,Canción 3')"
        db.execSQL(queryInsertExitosEspana)

        val queryInsertExitosMundiales = "INSERT INTO $TABLE_PLAYLISTS ($KEY_NOMBRE, $KEY_CANCIONES) VALUES ('Exitos Mundiales', 'Song 1,Song 2,Song 3')"
        db.execSQL(queryInsertExitosMundiales)

        val queryInsertUltimosLanzamientos = "INSERT INTO $TABLE_PLAYLISTS ($KEY_NOMBRE, $KEY_CANCIONES) VALUES ('Ultimos Lanzamientos', 'Track 1,Track 2,Track 3')"
        db.execSQL(queryInsertUltimosLanzamientos)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar las tablas si existen y crearlas de nuevo en caso de actualización de la base de datos
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CANCIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LIKES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYLISTS")
        onCreate(db)
    }

    fun insertUsuario(context: Context, username: String, password: String): Long {
        val dbHandler = SwipifyDatabase(context)
        val db = dbHandler.writableDatabase

        val contentValues = ContentValues().apply {
            put("usuario", username)
            put("contrasenya", password)
        }
        val result = db.insert("usuarios", null, contentValues)
        db.close()
        return result
    }

    fun loginUser(context: Context, username: String, password: String): String? {
        val dbHandler = SwipifyDatabase(context)
        val db = dbHandler.readableDatabase

        val query = "SELECT $KEY_USUARIO FROM $TABLE_USUARIOS WHERE $KEY_USUARIO = ? AND $KEY_CONTRASENYA = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val isLoggedIn = cursor.count > 0
        var loggedInUsername: String? = null
        if (isLoggedIn) {
            cursor.moveToFirst()
            loggedInUsername = cursor.getString(cursor.getColumnIndex(KEY_USUARIO))
        }
        cursor.close()
        return loggedInUsername
    }


    fun insertPlaylist(context: Context, nombre: String, canciones: String): Long {
        val dbHandler = SwipifyDatabase(context)
        val db = dbHandler.writableDatabase

        val contentValues = ContentValues().apply {
            put(KEY_NOMBRE, nombre)
            put(KEY_CANCIONES, canciones)
        }
        val result = db.insert(TABLE_PLAYLISTS, null, contentValues)
        db.close()
        return result
    }

    fun insertSongIntoPlaylist(context: Context, playlistId: Long, nombreCancion: String): Long {
        val dbHandler = SwipifyDatabase(context)
        val db = dbHandler.writableDatabase

        val contentValues = ContentValues().apply {
            put(KEY_ID, playlistId)
            put(KEY_NOMBRE, nombreCancion)
        }

        val result = db.insert(TABLE_PLAYLISTS, null, contentValues)
        db.close()

        return result
    }

    fun getAllPlaylists(context: Context): List<String> {
        val playlists = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $KEY_NOMBRE FROM $TABLE_PLAYLISTS", null)
        cursor.use {
            while (it.moveToNext()) {
                val playlistName = it.getString(it.getColumnIndex(KEY_NOMBRE))
                playlists.add(playlistName)
            }
        }
        cursor.close() // Cierra el cursor después de su uso
        return playlists
    }

}
