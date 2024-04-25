package com.example.swipifyapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginText: TextView
    lateinit var passwordRequirementsTextView: TextView

    lateinit var swipifyDatabase: SwipifyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        swipifyDatabase = SwipifyDatabase(this)

        usernameEditText = findViewById(R.id.editTextNewUsername)
        passwordEditText = findViewById(R.id.editTextNewPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        registerButton = findViewById(R.id.buttonRegister)
        loginText = findViewById(R.id.textViewLogin)
        passwordRequirementsTextView = findViewById(R.id.passwordRequirementsTextView) // Inicializar el TextView

        registerButton.setOnClickListener {
            register() // Llamada al método register sin parámetros
        }

        loginText.setOnClickListener {
            navigateToLogin()
        }
    }

    fun register() {
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

        // Verificar si la contraseña cumple con los requisitos
        if (!isValidPassword(password)) {
            passwordRequirementsTextView.visibility = View.VISIBLE // Cambiar la visibilidad a 'visible'
            return
        } else {
            passwordRequirementsTextView.visibility = View.INVISIBLE // Cambiar la visibilidad a 'invisible'
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

    fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    // Método para verificar si la contraseña cumple con los requisitos
    fun isValidPassword(password: String): Boolean {
        val upperCaseRegex = Regex("[A-Z]")
        val specialCharacterRegex = Regex("[^A-Za-z0-9]")
        val numberRegex = Regex("[0-9]")

        return password.length >= 8 && password.contains(upperCaseRegex) &&
                password.contains(specialCharacterRegex) && password.contains(numberRegex)
    }
}

