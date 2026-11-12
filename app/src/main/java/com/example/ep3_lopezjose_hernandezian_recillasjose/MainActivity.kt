package com.example.ep3_lopezjose_hernandezian_recillasjose

import android.content.Intent
//import android.credentials.CredentialManager
//import android.credentials.GetCredentialException
//import android.credentials.GetCredentialRequest
//import android.net.wifi.hotspot2.pps.Credential
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch


import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException

class MainActivity : AppCompatActivity() {
    private lateinit var etCorrero : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnIngresar : Button
    private lateinit var dbHelper : DatabaseHelper

    //Crear objetos para el funcionamiento con Google
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager : CredentialManager
    private lateinit var btnGoogle : ImageButton

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


        //hoy
        btnGoogle = findViewById(R.id.btnGoogle)
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this)


        //Evento para iniciar sesión
        btnIngresar.setOnClickListener {
            //llamar al método para iniciar sesion
            iniciarSesion()
        }

        btnGoogle.setOnClickListener {
            //
            iniciarSesionGoogle()
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

    // 3. MÉTODO PARA AUTENTICAR EN FIREBSAE
    private fun autenticaFirebase(idToken: String){

        val credencialFirebase = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credencialFirebase).addOnCompleteListener(this) {

                inicio ->
            //Validar si el inicio de sesion es correcto
            if(inicio.isSuccessful){
                //Crear variable para guardar al usuario actual
                val usuario = auth.currentUser

                Toast.makeText(this, "Bienvenido ${usuario?.displayName}", Toast.LENGTH_SHORT).show()
                //Enviar al usuario a HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

                finish()
            }else{
                Toast.makeText(this, "Error al iniciar con Firebase", Toast.LENGTH_SHORT).show()

            }

        }

    }

    // 4. MÉTODO PARA PROCESAR LA CREDENCIAL
    private fun procesarCredencial(credential: Credential){

        if(credential is CustomCredential && credential.type ==
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){

            try {
                //Validar la credencial del proveedor
                val credencialGoogle = GoogleIdTokenCredential.createFrom(
                    credential.data
                )
                //Iniciar proceso de autentificacion
                autenticaFirebase(credencialGoogle.idToken)
            }catch (e : GoogleIdTokenParsingException){
                Toast.makeText(this, "Credencial invalida", Toast.LENGTH_SHORT).show()

            }
        }
    }

    // 5. MÉTODO PARA INICIAR SESIÓN
    private fun iniciarSesionGoogle(){

        val inicioGoogle = GetSignInWithGoogleOption.Builder(
            getString(R.string.default_web_client_id)).build()

        val solicitud = GetCredentialRequest.Builder()
            .addCredentialOption(inicioGoogle)
            .build()
        //Proceso para iniciar con FireBase
        lifecycleScope.launch {
            try {
                //Veridicar que el resultado sea correcto
                val resultado = credentialManager.getCredential(
                    context= this@MainActivity,
                    request = solicitud
                )
                //Llamar al método que permite procesar la credencial
                procesarCredencial(resultado.credential)
            }catch (e: GetCredentialException){
                //Toast.makeText(this, "Error al iniciar", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@MainActivity, "Error al iniciar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}