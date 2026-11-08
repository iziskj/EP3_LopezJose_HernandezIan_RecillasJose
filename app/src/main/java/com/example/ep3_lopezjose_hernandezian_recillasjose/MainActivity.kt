package com.example.ep3_lopezjose_hernandezian_recillasjose

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var etCorrero : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnIngresar : Button
    private lateinit var dbHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //Apartir de auia el codigo
        val tvCrearCuenta = findViewById<TextView>(R.id.tvCrearCuenta)
        etCorrero = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPass)
        btnIngresar = findViewById(R.id.btnIngresar)
        dbHelper = DatabaseHelper(this)

        //Evento para iniciar sesión
        btnIngresar.setOnClickListener {
            //llamar al método para iniciar sesion
            iniciarSesion()
        }

        tvCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }
    }

    //Método para inicar sesión
    private fun iniciarSesion(){
        val correo = etCorrero.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        if(correo.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        //Llamar al método de inicio de sesión en la BD
        val resultado = dbHelper.iniciarSesion(correo, pass)

        //Validadr si el inicio de sesión fue correcto
        if (resultado){
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()

        }
    }
}