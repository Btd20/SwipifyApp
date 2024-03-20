package com.example.swipifyapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginText: TextView

    private lateinit var swipifyDatabase: SwipifyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        swipifyDatabase = SwipifyDatabase(this)

        usernameEditText = findViewById(R.id.editTextNewUsername)
        passwordEditText = findViewById(R.id.editTextNewPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        registerButton = findViewById(R.id.buttonRegister)
        loginText = findViewById(R.id.textViewLogin)

        registerButton.setOnClickListener {
            register() // Llamada al método register sin parámetros
        }

        loginText.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun register() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
            return
        }

        val result = swipifyDatabase.insertUsuario(this, username, password)

        if (result != -1L) {
            Toast.makeText(this, "Registro exitoso para $username", Toast.LENGTH_SHORT).show()

            // Navegar a la pantalla de inicio de sesión
            navigateToLogin()
        } else {
            Toast.makeText(this, "Usuario ya registrado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
