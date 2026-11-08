package com.example.ep3_lopezjose_hernandezian_recillasjose.network

import com.example.ep3_lopezjose_hernandezian_recillasjose.mode.RecetasResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecetasApi {
    //Buscar las recetas por nombre/tipo
    @GET("search.php")
    fun buscarRecetas(
        @Query("s") busqueda: String
    ): Call<RecetasResponse>

    //Filtrar las recetas por área/país
    @GET("filter.php")
    fun obtenerPorPais(
        @Query("a") pais: String
    ): Call<RecetasResponse>

    //Obtener los detalles completos de la receta por ID
    @GET("lookup.php")
    fun obtenerDetalleReceta(
        @Query("i") idMeal: String
    ): Call<RecetasResponse>
}