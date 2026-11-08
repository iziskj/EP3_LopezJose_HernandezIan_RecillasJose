package com.example.ep3_lopezjose_hernandezian_recillasjose

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ep3_lopezjose_hernandezian_recillasjose.mode.RecetasResponse
import com.example.ep3_lopezjose_hernandezian_recillasjose.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    private lateinit var imgPortadaDetalle: ImageView
    private lateinit var btnFavorito: ImageButton
    private lateinit var tvNombreEmpanadaDetalle: TextView
    private lateinit var tvPaisDetalle: TextView
    private lateinit var tvIngredientesDetalle: TextView
    private lateinit var tvInstruccionesDetalle: TextView
    private var esFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle)
        btnFavorito = findViewById(R.id.btnFavorito)
        tvNombreEmpanadaDetalle = findViewById(R.id.tvNombreEmpanadaDetalle)
        tvPaisDetalle = findViewById(R.id.tvPaisDetalle)
        tvIngredientesDetalle = findViewById(R.id.tvIngredientesDetalle)
        tvInstruccionesDetalle = findViewById(R.id.tvInstruccionesDetalle)

        mostrarInformacionInicial()
    }

    private fun mostrarInformacionInicial() {
        val idReceta = intent.getStringExtra("ID_RECETA") ?: ""
        val nombre = intent.getStringExtra("NOMBRE_RECETA") ?: "Empanada"
        val pais = intent.getStringExtra("PAIS_RECETA") ?: "Origen"
        val imagen = intent.getStringExtra("IMAGEN_RECETA")

        tvNombreEmpanadaDetalle.text = nombre
        tvPaisDetalle.text = "País: $pais"

        Glide.with(this)
            .load(imagen)
            .into(imgPortadaDetalle)

        btnFavorito.setOnClickListener {
            esFavorito = !esFavorito
            if (esFavorito) {
                btnFavorito.setImageResource(R.drawable.quitarfavorito)
                Toast.makeText(this, "Guardado en recetas favoritas", Toast.LENGTH_SHORT).show()
            } else {
                btnFavorito.setImageResource(R.drawable.agregarfavorito)
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
            }
        }

        if (idReceta.isNotEmpty()) {
            obtenerDetallesCompletos(idReceta)
        }
    }

    private fun obtenerDetallesCompletos(idReceta: String) {
        RetrofitClient.api.obtenerDetalleReceta(idReceta).enqueue(object : Callback<RecetasResponse> {
            override fun onResponse(call: Call<RecetasResponse>, response: Response<RecetasResponse>) {
                val receta = response.body()?.results?.firstOrNull()
                if (receta != null) {
                    tvIngredientesDetalle.text = receta.obtenerIngredientesFormateados()
                    tvInstruccionesDetalle.text = receta.instrucciones ?: "Mezclar los ingredientes del relleno, rellenar la masa en forma de media luna y hornear o freír a fuego medio hasta dorar."
                } else {
                    cargarRecetaPorDefecto()
                }
            }

            override fun onFailure(call: Call<RecetasResponse>, t: Throwable) {
                cargarRecetaPorDefecto()
            }
        })
    }

    private fun cargarRecetaPorDefecto() {
        tvIngredientesDetalle.text = "• Masa para empanadas\n• Carne molida o guisado tradicional\n• Cebolla y especias al gusto\n• Aceite para freír"
        tvInstruccionesDetalle.text = "1. Preparar el relleno sazonando con las especias.\n2. Colocar una porción de relleno en el centro del disco de masa.\n3. Doblar a la mitad y cerrar los bordes (repulgue).\n4. Freír en abundante aceite caliente o hornear a 180°C durante 20 minutos."
    }
}