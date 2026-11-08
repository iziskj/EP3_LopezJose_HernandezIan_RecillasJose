package com.example.ep3_lopezjose_hernandezian_recillasjose

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ep3_lopezjose_hernandezian_recillasjose.mode.Empanada
import com.example.ep3_lopezjose_hernandezian_recillasjose.mode.RecetasResponse
import com.example.ep3_lopezjose_hernandezian_recillasjose.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var tvResultado: TextView
    private lateinit var contenedorMexico: LinearLayout
    private lateinit var contenedorEspana: LinearLayout
    private lateinit var contenedorColombia: LinearLayout
    private lateinit var contenedorArgentina: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        tvResultado = findViewById(R.id.tvResultado)
        contenedorMexico = findViewById(R.id.contenedorMexico)
        contenedorEspana = findViewById(R.id.contenedorEspana)
        contenedorColombia = findViewById(R.id.contenedorColombia)
        contenedorArgentina = findViewById(R.id.contenedorArgentina)

        //Cargar las recetas por país (Utilizando los códigos de área de TheMealDB o búsqueda temática)
        obtenerEmpanadasPorPais("Mexican", "México", contenedorMexico)
        obtenerEmpanadasPorPais("Spanish", "España", contenedorEspana)
        obtenerEmpanadasPorPais("Colombian", "Colombia", contenedorColombia)
        obtenerEmpanadasPorPais("Argentinian", "Argentina", contenedorArgentina)
    }

    private fun obtenerEmpanadasPorPais(codigoArea: String, nombrePais: String, contenedor: LinearLayout) {
        RetrofitClient.api.obtenerPorPais(codigoArea).enqueue(object : Callback<RecetasResponse> {
            override fun onResponse(call: Call<RecetasResponse>, response: Response<RecetasResponse>) {
                val recetas = response.body()?.results

                if (!recetas.isNullOrEmpty()) {
                    tvResultado.visibility = View.GONE
                    mostrarEmpanadas(recetas, nombrePais, contenedor)
                } else {
                    obtenerEmpanadasPorBusqueda(nombrePais, contenedor)
                }
            }

            override fun onFailure(call: Call<RecetasResponse>, t: Throwable) {
                obtenerEmpanadasPorBusqueda(nombrePais, contenedor)
            }
        })
    }

    private fun obtenerEmpanadasPorBusqueda(nombrePais: String, contenedor: LinearLayout) {
        RetrofitClient.api.buscarRecetas("empanada").enqueue(object : Callback<RecetasResponse> {
            override fun onResponse(call: Call<RecetasResponse>, response: Response<RecetasResponse>) {
                val recetas = response.body()?.results ?: emptyList()
                if (recetas.isNotEmpty()) {
                    tvResultado.visibility = View.GONE
                    mostrarEmpanadas(recetas, nombrePais, contenedor)
                }
            }

            override fun onFailure(call: Call<RecetasResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error al conectar con el API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarEmpanadas(empanadas: List<Empanada>, nombrePais: String, contenedor: LinearLayout) {
        contenedor.removeAllViews()

        //Limitar la cantidad de recetas que se muestran
        val empanadasLimitadas = empanadas.take(5)

        for (empanada in empanadasLimitadas) {
            val vistaEmpanada = layoutInflater.inflate(R.layout.item_album, contenedor, false)

            val imgPortada = vistaEmpanada.findViewById<ImageView>(R.id.imgPortadaAlbum)
            val tvNombre = vistaEmpanada.findViewById<TextView>(R.id.tvNombreAlbum)

            tvNombre.text = empanada.nombre ?: "Empanada Tradicional"

            Glide.with(this@HomeActivity)
                .load(empanada.imagen)
                .into(imgPortada)

            vistaEmpanada.setOnClickListener {
                val intent = Intent(this@HomeActivity, DetailsActivity::class.java)
                intent.putExtra("ID_RECETA", empanada.id)
                intent.putExtra("NOMBRE_RECETA", empanada.nombre)
                intent.putExtra("PAIS_RECETA", nombrePais)
                intent.putExtra("IMAGEN_RECETA", empanada.imagen)
                startActivity(intent)
            }

            contenedor.addView(vistaEmpanada)
        }
    }
}