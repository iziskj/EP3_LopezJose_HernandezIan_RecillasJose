package com.example.ep3_lopezjose_hernandezian_recillasjose.mode

import com.google.gson.annotations.SerializedName

data class Empanada(
    @SerializedName("idMeal") val id: String?,
    @SerializedName("strMeal") val nombre: String?,
    @SerializedName("strArea") val pais: String?,
    @SerializedName("strMealThumb") val imagen: String?,
    @SerializedName("strInstructions") val instrucciones: String?,
    @SerializedName("strIngredient1") val ingrediente1: String?,
    @SerializedName("strIngredient2") val ingrediente2: String?,
    @SerializedName("strIngredient3") val ingrediente3: String?,
    @SerializedName("strIngredient4") val ingrediente4: String?,
    @SerializedName("strIngredient5") val ingrediente5: String?,
    @SerializedName("strMeasure1") val medida1: String?,
    @SerializedName("strMeasure2") val medida2: String?,
    @SerializedName("strMeasure3") val medida3: String?,
    @SerializedName("strMeasure4") val medida4: String?,
    @SerializedName("strMeasure5") val medida5: String?
) {
    //Genera la lista de ingredientes
    fun obtenerIngredientesFormateados(): String {
        val lista = mutableListOf<String>()
        if (!ingrediente1.isNullOrBlank()) lista.add("• ${medida1 ?: ""} $ingrediente1")
        if (!ingrediente2.isNullOrBlank()) lista.add("• ${medida2 ?: ""} $ingrediente2")
        if (!ingrediente3.isNullOrBlank()) lista.add("• ${medida3 ?: ""} $ingrediente3")
        if (!ingrediente4.isNullOrBlank()) lista.add("• ${medida4 ?: ""} $ingrediente4")
        if (!ingrediente5.isNullOrBlank()) lista.add("• ${medida5 ?: ""} $ingrediente5")

        return if (lista.isNotEmpty()) lista.joinToString("\n") else "Ingredientes variados según la receta tradicional."
    }
}