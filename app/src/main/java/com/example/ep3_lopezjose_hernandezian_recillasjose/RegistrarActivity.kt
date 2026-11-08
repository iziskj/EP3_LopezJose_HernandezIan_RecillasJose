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

class RegistrarActivity : AppCompatActivity() {
    //AQUI SE DECLARAN LOS ELEMENTOS
    private lateinit var etNombre : EditText
    private lateinit var etCorreo : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnRegistrar : Button

    //Crear instancia del DatabaseHelper
    private lateinit var dbHelper : DatabaseHelper
    //MÉTODO PRINCIPAL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        //Inicializar los objetos de los componentes
        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPass)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        dbHelper = DatabaseHelper(this)
        //Evento para registrar al usuario
        btnRegistrar.setOnClickListener {
            //Registrar al usuario
            registrarUsuario()
        }
        //Cambiar de ventana
        val tvIniciarSesion = findViewById<TextView>(R.id.tvIniciarSesion)
        tvIniciarSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun registrarUsuario(){
        //Variables para gusrdar los textos
        val nombre = etNombre.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val password= etPassword.text.toString().trim()
        //Validar que no esten vacios
        if(nombre.isEmpty() || correo.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        //Variable que guarda el resultado del registro y llama a la BD
        val registro = dbHelper.registrarUsuario(nombre, correo, password)

        //Validar que el resultado de la inserción sea verdadero o falso
        if(registro){
            //Si el registro es correcto
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            //Cierra la pantalla actual y regresa a la pantalla anterior
            finish()
        }else{
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
        }
    }
}